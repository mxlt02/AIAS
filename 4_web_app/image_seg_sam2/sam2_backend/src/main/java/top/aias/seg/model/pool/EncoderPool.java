package top.aias.seg.model.pool;// 导入需要的包

import ai.djl.inference.Predictor;
import ai.djl.ndarray.NDList;
import ai.djl.repository.zoo.ZooModel;
import top.aias.seg.bean.Sam2Input;

import java.util.ArrayList;

/**
 * 图像分割连接池
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public class EncoderPool {
    private int poolSize;
    private ZooModel<Sam2Input, NDList> model;
    private ArrayList<Predictor<Sam2Input, NDList>> predictorList = new ArrayList<>();


    public EncoderPool(ZooModel<Sam2Input, NDList> model, int poolSize) {
        this.poolSize = poolSize;
        this.model = model;

        for (int i = 0; i < poolSize; i++) {
            Predictor<Sam2Input, NDList> detector = model.newPredictor();
            predictorList.add(detector);
        }
    }

    public synchronized Predictor<Sam2Input, NDList> getPredictor() {
        while (predictorList.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Predictor<Sam2Input, NDList> predictor = predictorList.remove(0);
        return predictor;
    }

    public synchronized void releasePredictor(Predictor<Sam2Input, NDList> predictor) {
        predictorList.add(predictor);
        notifyAll();
    }

    public void close() {
        for (Predictor<Sam2Input, NDList> predictor : predictorList) {
            predictor.close();
        }

    }
}