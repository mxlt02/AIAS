package top.aias.platform.model.sd.controlnet;

import ai.djl.Device;
import ai.djl.MalformedModelException;
import ai.djl.inference.Predictor;
import ai.djl.ndarray.NDList;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.NoopTranslator;
import ai.djl.translate.TranslateException;
import top.aias.platform.model.sd.pool.ControlNetPool;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * ControlNet 模型
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public class BaseModel implements AutoCloseable {
    private ZooModel<NDList, NDList> model;
    private Device device;
    private ControlNetPool pool;
    private String root;
    private String modelName;
    private int poolSize;
    private final AtomicBoolean initialized = new AtomicBoolean(false);

    public BaseModel(String root, String modelName, int poolSize, Device device) {
        this.root = root;
        this.modelName = modelName;
        this.poolSize = poolSize;
        this.device = device;
    }

    public synchronized void ensureInitialized() {
        if (!initialized.get()) {
            try {
                this.model = ModelZoo.loadModel(criteria());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ModelNotFoundException e) {
                e.printStackTrace();
            } catch (MalformedModelException e) {
                e.printStackTrace();
            }
            this.pool = new ControlNetPool(model, poolSize);
            initialized.set(true);
        }
    }

    public NDList predict(NDList ndList) throws TranslateException {
        ensureInitialized();
        Predictor<NDList, NDList> predictor = pool.getPredictor();
        NDList result = predictor.predict(ndList);
        pool.releasePredictor(predictor);
        return result;
    }

    public void close() {
        if (initialized.get()) {
            model.close();
            pool.close();
        }
    }

    public Criteria<NDList, NDList> criteria() {
        Criteria<NDList, NDList> criteria =
                Criteria.builder()
                        .setTypes(NDList.class, NDList.class)
                        .optModelPath(Paths.get(root + modelName))
                        .optEngine("PyTorch")
                        .optProgress(new ProgressBar())
                        .optTranslator(new NoopTranslator())
                        .optDevice(device)
                        .build();

        return criteria;
    }
}
