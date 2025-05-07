### 官网：
[官网链接](http://www.aias.top/)

### 下载模型
- 查看最新下载链接请查看 1_sdks/README.md

### 模型使用方法：
- 1. 用模型的名字搜索代码，找到模型的加载位置
- 2. 然后更新模型路径（代码里默认加载路径是：项目/models 文件夹）
- 3. 具体模型加载方法
- http://aias.top/AIAS/guides/load_model.html

### 人脸特征提取与比对SDK
人工智能人脸特征提取是一种复杂而重要的计算机技术，其主要目的是通过对人脸图像进行深度分析和处理，提取出具有代表性的特征信息，以用于实现人脸识别、人脸比对、人脸验证等应用。这个技术的核心在于将人脸图像转化为计算机能够理解的数字特征，这些特征可以被用于训练人工智能模型，从而提高模型的准确性和性能。
人脸特征提取技术是一项非常重要的技术，在现代生活中被广泛应用于安防、金融、医疗等领域。在安防领域，人脸特征提取技术可以用于实现人脸识别、身份验证等功能，提高社会安全。在金融领域，人脸特征提取技术可以用于实现客户身份验证和授权，以确保金融交易的安全性和可靠性。在医疗领域，人脸特征提取技术可以用于实现医生和患者身份认证，提高医疗服务的质量和效率。
总之，人工智能人脸特征提取技术是一种非常重要的技术，其应用范围非常广泛。随着科技的不断进步和创新，这项技术将会在更多的领域得到应用，为人们带来更加便捷、高效、安全的生活体验。

#### 人脸识别关键技术
人脸识别涉及的关键技术包含：人脸检测，人脸关键点，人脸特征提取，人脸比对，人脸对齐。
![face_sdk](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/face_sdk/images/face_sdk.png)

本文的例子给出了人脸特征提取，人脸比对的参考实现。
人脸识别完整的pipeline：人脸检测(含人脸关键点) --> 人脸对齐 --> 人脸特征提取 --> 人脸比对

#### 人脸特征提取：

#### 运行人脸特征提取的例子 - FeatureExtractionExample
运行成功后，命令行应该看到下面的信息:
```text
[INFO ] - Face feature: [-0.04026184, -0.019486362, -0.09802659, 0.01700999, 0.037829027, ...]
```

#### 运行人脸特征比对的例子 - FeatureComparisonExample
人脸识别完整的pipeline：人脸检测(含人脸关键点) --> 人脸对齐 --> 人脸特征提取 --> 人脸比对
- 首先检测人脸
- 然后根据人脸关键点转正对齐
- 提取特征比较相似度   
  ![face_feature](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/face_sdk/images/face_feature.png)

运行成功后，命令行应该看到下面的信息:  
比对使用的是余弦相似度的计算方式。
提取特征计算相似度。可以进一步对112 * 112 人脸图片按比例裁剪，去除冗余信息，比如头发等，以提高精度。
如果图片模糊，可以结合人脸超分辨模型使用。
```text
[INFO ] - face1 feature: [0.19923544, 0.2091935, -0.17899065, ..., 0.7100589, -0.27192503, 1.1901716]
[INFO ] - face2 feature: [0.1881579, -0.40177754, -0.19950306, ..., -0.71886086, 0.31257823, -0.009294844]
[INFO ] - Similarity： 0.75430954
```

### 开源算法
#### 1. sdk使用的开源算法
- [RetinaFaceDetection - Pytorch_Retinaface](https://github.com/biubug6/Pytorch_Retinaface)
- [Face Feature](https://www.modelscope.cn/models/damo/cv_ir101_facerecognition_cfglint/summary)


#### 帮助文档：
- http://aias.top/guides.html
- 1.性能优化常见问题:
- http://aias.top/AIAS/guides/performance.html
- 2.引擎配置（包括CPU，GPU在线自动加载，及本地配置）:
- http://aias.top/AIAS/guides/engine_config.html
- 3.模型加载方式（在线自动加载，及本地配置）:
- http://aias.top/AIAS/guides/load_model.html
- 4.Windows环境常见问题:
- http://aias.top/AIAS/guides/windows.html