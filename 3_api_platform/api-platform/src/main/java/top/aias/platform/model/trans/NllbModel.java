package top.aias.platform.model.trans;

import ai.djl.Device;
import ai.djl.MalformedModelException;
import ai.djl.huggingface.tokenizers.Encoding;
import ai.djl.huggingface.tokenizers.HuggingFaceTokenizer;
import ai.djl.inference.Predictor;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.index.NDIndex;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.NoopTranslator;
import ai.djl.translate.TranslateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.aias.platform.generate.GreedyBatchTensorList;
import top.aias.platform.generate.LMOutput;
import top.aias.platform.generate.TransConfig;
import top.aias.platform.utils.TokenUtils;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 文本翻译模型，支持202种语言互译
 *
 * @author calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 **/
public final class NllbModel implements AutoCloseable {
    private static final Logger logger = LoggerFactory.getLogger(NllbModel.class);
    private TransConfig config;
    private ZooModel<NDList, NDList> model;
    private TextEncoderPool encoderPool;
    private TextDecoderPool decoderPool;
    private TextDecoder2Pool decoder2Pool;
    private NDManager manager;
    private HuggingFaceTokenizer tokenizer;
    private String modelPath;
    private String modelName;
    private int poolSize;
    private Device device;
    private final AtomicBoolean initialized = new AtomicBoolean(false);

    public NllbModel(TransConfig config, String modelPath, String modelName, int poolSize, Device device) {
        this.config = config;
        this.modelPath = modelPath;
        this.modelName = modelName;
        this.poolSize = poolSize;
        this.device = device;
    }

    public synchronized void ensureInitialized() {
        if (!initialized.get()) {
            Criteria<NDList, NDList> criteria =
                    Criteria.builder()
                            .setTypes(NDList.class, NDList.class)
                            .optModelPath(Paths.get(modelPath + modelName))
                            .optEngine("PyTorch")
                            .optDevice(device)
                            .optTranslator(new NoopTranslator())
                            .build();

            try {
                this.model = criteria.loadModel();
                manager = NDManager.newBaseManager(device);
                tokenizer = HuggingFaceTokenizer.newInstance(Paths.get(modelPath + "tokenizer.json"));
                this.encoderPool = new TextEncoderPool(model, poolSize);
                this.decoderPool = new TextDecoderPool(model, poolSize);
                this.decoder2Pool = new TextDecoder2Pool(model, poolSize);
                initialized.set(true);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ModelNotFoundException e) {
                e.printStackTrace();
            } catch (MalformedModelException e) {
                e.printStackTrace();
            }
        }
    }

    private NDArray encoder(long[] ids) throws TranslateException {
        Predictor<long[], NDArray> predictor = encoderPool.getPredictor();
        NDArray result = predictor.predict(ids);
        encoderPool.releasePredictor(predictor);
        return result;
    }

    private LMOutput decoder(NDList input) throws TranslateException {
        Predictor<NDList, LMOutput> predictor = decoderPool.getPredictor();
        LMOutput result = predictor.predict(input);
        decoderPool.releasePredictor(predictor);
        return result;
    }

    private LMOutput decoder2(NDList input) throws TranslateException {
        Predictor<NDList, LMOutput> predictor = decoder2Pool.getPredictor();
        LMOutput result = predictor.predict(input);
        decoder2Pool.releasePredictor(predictor);
        return result;
    }

    public void close() {
        if (initialized.get()) {
            this.model.close();
            this.encoderPool.close();
            this.decoderPool.close();
            this.decoder2Pool.close();
            manager.close();
            tokenizer.close();
        }
    }

    public String translate(String input, long srcLangId, long targetLangId) throws TranslateException {
        ensureInitialized();

        // 源语言：如中文 "zho_Hans": 256200
        config.setSrcLangId(srcLangId);
        // 目标语言：如英文 "eng_Latn": 256047
        config.setForcedBosTokenId(targetLangId);

        Encoding encoding = tokenizer.encode(input);
        long[] ids = encoding.getIds();
        // 1. Encoder
        long[] inputIds = new long[ids.length];
        // 设置源语言编码
        inputIds[0] = config.getSrcLangId();
        for (int i = 0; i < ids.length - 1; i++) {
            inputIds[i + 1] = ids[i];
        }
        logger.info("inputIds: " + Arrays.toString(inputIds));
        long[] attentionMask = encoding.getAttentionMask();
        NDArray attentionMaskArray = manager.create(attentionMask).expandDims(0);

        NDArray encoderHiddenStates = encoder(inputIds);

        NDArray decoder_input_ids = manager.create(new long[]{config.getDecoderStartTokenId()}).reshape(1, 1);
        NDList decoderInput = new NDList(decoder_input_ids, encoderHiddenStates, attentionMaskArray);

        // 2. Initial Decoder
        LMOutput modelOutput = decoder(decoderInput);
        modelOutput.getLogits().attach(manager);
        modelOutput.getPastKeyValuesList().attach(manager);

        GreedyBatchTensorList searchState =
                new GreedyBatchTensorList(null, decoder_input_ids, modelOutput.getPastKeyValuesList(), encoderHiddenStates, attentionMaskArray);

        while (true) {
//            try (NDScope ignore = new NDScope()) {
            NDArray pastOutputIds = searchState.getPastOutputIds();

            if (searchState.getNextInputIds() != null) {
                decoderInput = new NDList(searchState.getNextInputIds(), searchState.getEncoderHiddenStates(), searchState.getAttentionMask());
                decoderInput.addAll(searchState.getPastKeyValues());
                // 3. Decoder loop
                modelOutput = decoder2(decoderInput);
            }

            NDArray outputIds = greedyStepGen(config, pastOutputIds, modelOutput.getLogits());

            searchState.setNextInputIds(outputIds);
            pastOutputIds = pastOutputIds.concat(outputIds, 1);
            searchState.setPastOutputIds(pastOutputIds);

            searchState.setPastKeyValues(modelOutput.getPastKeyValuesList());

            // memory management
//                NDScope.unregister(outputIds, pastOutputIds);
//        }

            // Termination Criteria
            long id = searchState.getNextInputIds().toLongArray()[0];
            if (config.getEosTokenId() == id) {
                searchState.setNextInputIds(null);
                break;
            }
            if (searchState.getPastOutputIds() != null && searchState.getPastOutputIds().getShape().get(1) + 1 >= config.getMaxSeqLength()) {
                break;
            }
        }

        if (searchState.getNextInputIds() == null) {
            NDArray resultIds = searchState.getPastOutputIds();
            String result = TokenUtils.decode(config, tokenizer, resultIds);
            return result;
        } else {
            NDArray resultIds = searchState.getPastOutputIds(); // .concat(searchState.getNextInputIds(), 1)
            String result = TokenUtils.decode(config, tokenizer, resultIds);
            return result;
        }
    }

    private NDArray greedyStepGen(TransConfig config, NDArray pastOutputIds, NDArray next_token_scores) {
        next_token_scores = next_token_scores.get(":, -1, :");

        NDArray new_next_token_scores = manager.create(next_token_scores.getShape(), next_token_scores.getDataType());
        next_token_scores.copyTo(new_next_token_scores);

        // LogitsProcessor 1. ForcedBOSTokenLogitsProcessor
        // 设置目标语言
        long cur_len = pastOutputIds.getShape().getLastDimension();
        if (cur_len == 1) {
            long num_tokens = new_next_token_scores.getShape().getLastDimension();
            for (long i = 0; i < num_tokens; i++) {
                if (i != config.getForcedBosTokenId()) {
                    new_next_token_scores.set(new NDIndex(":," + i), Float.NEGATIVE_INFINITY);
                }
            }
            new_next_token_scores.set(new NDIndex(":," + config.getForcedBosTokenId()), 0);
        }

        NDArray probs = new_next_token_scores.softmax(-1);
        NDArray next_tokens = probs.argMax(-1);

        return next_tokens.expandDims(0);
    }
}
