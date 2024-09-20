package top.aias.ocr;

import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.TranslateException;
import top.aias.ocr.utils.common.ImageUtils;
import top.aias.ocr.utils.layout.LayoutDetection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * 中英文表格检测.
 * Table Detection
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public final class TableDetExample {

  private static final Logger logger = LoggerFactory.getLogger(TableDetExample.class);

  private TableDetExample() {}

  public static void main(String[] args) throws IOException, ModelException, TranslateException {
    Path imageFile = Paths.get("src/test/resources/3.png");
    Image image = ImageFactory.getInstance().fromFile(imageFile);

    LayoutDetection detection = new LayoutDetection();
    try (ZooModel detectionModel = ModelZoo.loadModel(detection.tableCriteria());
        Predictor<Image, DetectedObjects> detector = detectionModel.newPredictor()) {

      DetectedObjects detections = detector.predict(image);

      List<DetectedObjects.DetectedObject> boxes = detections.items();
      for (DetectedObjects.DetectedObject result : boxes) {
        System.out.println(result.getClassName() + " : " + result.getProbability());
      }

      ImageUtils.saveBoundingBoxImage(
          image, detections, "table_detect_result.png", "build/output");
      logger.info("{}", detections);
    }
  }
}