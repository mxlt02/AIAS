package top.aias.platform.model.trans;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.translate.NoBatchifyTranslator;
import ai.djl.translate.TranslatorContext;
import top.aias.platform.generate.LMOutput;

/**
 * 解碼器，參數沒有 pastKeyValues
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public class DecoderTranslator implements NoBatchifyTranslator<NDList, LMOutput> {
    private String tupleName;

    public DecoderTranslator() {
        tupleName = "past_key_values(" + 12 + ',' + 4 + ')';
    }

    @Override
    public NDList processInput(TranslatorContext ctx, NDList input) {

        NDArray placeholder = ctx.getNDManager().create(0);
        placeholder.setName("module_method:decoder");

        input.add(placeholder);

        return input;
    }

    @Override
    public LMOutput processOutput(TranslatorContext ctx, NDList output) {
        NDArray logitsOutput = output.get(0);
        NDList pastKeyValuesOutput = output.subNDList(1, 12 * 4 + 1);

        for (NDArray array : pastKeyValuesOutput) {
            array.setName(tupleName);
        }

        return new LMOutput(logitsOutput, pastKeyValuesOutput);
    }
}