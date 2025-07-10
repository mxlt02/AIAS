package top.aias.seg.translator;

import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.transform.Normalize;
import ai.djl.modality.cv.transform.Resize;
import ai.djl.modality.cv.transform.ToTensor;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.translate.NoBatchifyTranslator;
import ai.djl.translate.Pipeline;
import ai.djl.translate.TranslatorContext;
import top.aias.seg.bean.Sam2Input;

/**
 * sam2 编码器前后处理
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public class Sam2EncoderTranslator implements NoBatchifyTranslator<Sam2Input, NDList> {

    private static final float[] MEAN = {0.485f, 0.456f, 0.406f};
    private static final float[] STD = {0.229f, 0.224f, 0.225f};

    private Pipeline pipeline;

    public Sam2EncoderTranslator() {
        pipeline = new Pipeline();
        pipeline.add(new Resize(1024, 1024));
        pipeline.add(new ToTensor());
        pipeline.add(new Normalize(MEAN, STD));
    }

    @Override
    public NDList processInput(TranslatorContext ctx, Sam2Input input) {
        Image image = input.getImage();
        int width = image.getWidth();
        int height = image.getHeight();
        ctx.setAttachment("width", width);
        ctx.setAttachment("height", height);

        NDManager manager = ctx.getNDManager();
        NDArray array = image.toNDArray(manager, Image.Flag.COLOR);
        array = pipeline.transform(new NDList(array)).get(0).expandDims(0);

        return new NDList(array);
    }

    @Override
    public NDList processOutput(TranslatorContext ctx, NDList list) {
        NDArray logits = list.get(0);

        list.detach();
        return list;
    }
}