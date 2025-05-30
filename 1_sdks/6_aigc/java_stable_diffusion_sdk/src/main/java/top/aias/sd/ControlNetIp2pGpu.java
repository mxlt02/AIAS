package top.aias.sd;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.translate.TranslateException;
import top.aias.sd.pipelines.StableDiffusionControlNetPipeline;
import top.aias.sd.utils.ImageUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class ControlNetIp2pGpu {

    private ControlNetIp2pGpu() {}

    public static void main(String[] args) throws ModelException, IOException, TranslateException {

        Path imageFile = Paths.get("src/test/resources/ip2p.png");
        Image image = OpenCVImageFactory.getInstance().fromFile(imageFile);
        String prompt = "make it on fire";

        try(StableDiffusionControlNetPipeline model = new StableDiffusionControlNetPipeline("H:\\models\\aigc\\sd_gpu\\", "controlnet_ip2p.pt", Device.gpu());){
            Image result = model.generateImage(image, prompt,"",  25);
            ImageUtils.saveImage(result, "ctrlnet_ip2p_pt_gpu.png", "build/output");
        }
    }
}