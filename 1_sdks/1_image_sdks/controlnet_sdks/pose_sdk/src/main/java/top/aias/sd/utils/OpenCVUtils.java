package top.aias.sd.utils;

import org.opencv.core.CvType;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.Mat;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
/**
 * OpenCV 工具类
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public class OpenCVUtils {
    public static Mat perspectiveTransform(Mat src, Mat srcPoints, Mat dstPoints) {
        Mat dst = src.clone();
        Mat warp_mat = Imgproc.getPerspectiveTransform(srcPoints, dstPoints);
        Imgproc.warpPerspective(src, dst, warp_mat, dst.size());
        warp_mat.release();

        return dst;
    }

    /**
     * canny算法，边缘检测
     *
     * @param src
     * @return
     */
    public static org.opencv.core.Mat canny(org.opencv.core.Mat src) {
        org.opencv.core.Mat mat = src.clone();
        Imgproc.Canny(src, mat, 100, 200);
        return mat;
    }

    public static org.opencv.core.Mat sobel(org.opencv.core.Mat src, int dx, int dy) {
        org.opencv.core.Mat mat = src.clone();
        Imgproc.Sobel(src, mat, CvType.CV_32F, dx, dy, 3);//CvType.CV_32F  CV_64F scale：默认1 delta：默认0
//        org.opencv.core.Mat dst = mat.clone();
//        Core.convertScaleAbs(mat, dst);
        return mat;
    }

    public static org.opencv.core.Mat remap(org.opencv.core.Mat src, org.opencv.core.Mat map1, org.opencv.core.Mat map2) {
        org.opencv.core.Mat dst = src.clone();
        Imgproc.remap(src, dst, map1, map2, Imgproc.INTER_LINEAR);
        return dst;
    }

    public static void line(org.opencv.core.Mat mat, org.opencv.core.Point point1, org.opencv.core.Point point2) {
        Imgproc.line(mat, point1, point2, new Scalar(255, 255, 255), 1);
    }

    /**
     * Mat to BufferedImage
     *
     * @param mat
     * @return
     */
    public static BufferedImage mat2Image(org.opencv.core.Mat mat) {
        int width = mat.width();
        int height = mat.height();
        byte[] data = new byte[width * height * (int) mat.elemSize()];
        Imgproc.cvtColor(mat, mat, 4);
        mat.get(0, 0, data);
        BufferedImage ret = new BufferedImage(width, height, 5);
        ret.getRaster().setDataElements(0, 0, width, height, data);
        return ret;
    }

    public static BufferedImage matToBufferedImage(org.opencv.core.Mat frame) {
        int type = 0;
        if (frame.channels() == 1) {
            type = BufferedImage.TYPE_BYTE_GRAY;
        } else if (frame.channels() == 3) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        BufferedImage image = new BufferedImage(frame.width(), frame.height(), type);
        WritableRaster raster = image.getRaster();
        DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();
        byte[] data = dataBuffer.getData();
        frame.get(0, 0, data);
        return image;
    }

    /**
     * BufferedImage to Mat
     *
     * @param img
     * @return
     */
    public static org.opencv.core.Mat image2Mat(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();
        byte[] data = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
        org.opencv.core.Mat mat = new org.opencv.core.Mat(height, width, CvType.CV_8UC3);
        mat.put(0, 0, data);
        return mat;
    }
}