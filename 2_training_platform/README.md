
### 数据与预训练模型下载
- 链接: https://pan.baidu.com/s/1Y-Yeam-opZx0dM5hcLzgJg?pwd=s3j3

### AI 训练平台
AI训练平台提供分类模型训练能力。并以REST API形式为上层应用提供接口。
当前版包含功能如下：
-分类模型训练 （imagenet数据集预训练的resnet50模型）
-模型训练可视化
-图片分类推理
-图片特征提取（512维特征）
-图片 1:1 比对

## 前端部署

#### nginx部署运行：
```bash
cd /usr/local/etc/nginx/
vi /usr/local/etc/nginx/nginx.conf
# 编辑nginx.conf

    server {
        listen       8080;
        server_name  localhost;

        location / {
            root   /Users/calvin/platform/dist/;
            index  index.html index.htm;
        }
     ......
     
# 重新加载配置：
sudo nginx -s reload 

# 部署应用后，重启：
cd /usr/local/Cellar/nginx/1.19.6/bin

# 快速停止
sudo nginx -s stop

# 启动
sudo nginx     
```


## 后端部署
```bash
# 编译 & 运行程序
nohup java -Dfile.encoding=utf-8 -jar xxxxx.jar > log.txt 2>&1 &

```

## 打开浏览器

输入地址： http://localhost:8090

#### 1. 训练数据准备-ZIP格式压缩包


#### 2. 上传数据并开始训练:
-选择zip文件并上传
-点击训练按钮开始训练
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/train_platform/images/storage.png)

#### 3. 查看训练过程:  
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/train_platform/images/training.png)
  
#### 4. 图片分类测试:  
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/train_platform/images/classification.png)
  
#### 5. 特征提取测试: 
图片特征提取使用的是新训练的模型。特征来自模型的特征提取层。 
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/train_platform/images/feature.png)

#### 6. 图片比对测试:  
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/train_platform/images/comparision.png)


  
#### 编辑jar包中的application.yml
根据需要编辑application.yml中的图片上传路径,模型保存路径
（windows环境可以使用7-zip直接编辑，无需对jar包解压缩重新压缩）
```bash
# 文件存储路径
file:
  mac:
    path: ~/file/
    modelPath: /Users/calvin/AIAS/2_training_platform/platform-train/models/resnet50_dl4j_inference.v3.zip
    dataRootPath: /Users/calvin/AIAS/2_training_platform/platform-train/data/ #压缩包解压缩文件夹
    savePath: /Users/calvin/AIAS/2_training_platform/platform-train/models/NewResNet50.zip #模型训练好后存放的路径
  linux:
    path: /home/aias/file/
    modelPath: /home/aias/file/resnet50_dl4j_inference.v3.zip
    dataRootPath: /home/aias/file/image_root/ #压缩包解压缩文件夹
    savePath: /home/aias/file//model/NewResNet50.zip #模型训练好后存放的路径
  windows:
    path: C:\\aias\\file\\
    modelPath: C:\\aias\\file\\resnet50_dl4j_inference.v3.zip
    dataRootPath: C:\\aias\\file\\image_root\\ #压缩包解压缩文件夹
    savePath: C:\\aias\\file\\modelv2\\NewResNet50.zip #模型训练好后存放的路径
  # 文件大小 /M
  maxSize: 3000
```


### 官网：
[官网链接](http://www.aias.top/)

### Git地址：   
[Github链接](https://github.com/mymagicpower/AIAS)    
[Gitee链接](https://gitee.com/mymagicpower/AIAS)   



