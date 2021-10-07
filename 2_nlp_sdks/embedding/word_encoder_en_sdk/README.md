# 词向量SDK【英文】
词向量/词嵌入（Word embedding）是自然语言处理（NLP）中语言模型与表征学习技术的统称。
概念上而言，它是指把一个维数为所有词的数量的高维空间嵌入到一个维数低得多的连续向量空间中，
每个单词或词组被映射为实数域上的向量。


- 词向量
![img](https://djl-model.oss-cn-hongkong.aliyuncs.com/AIAS/nlp_sdks/word_vector_en.png)

### SDK包含5个模型数据：
-  glove_wiki2014_gigaword_dim50 (WordEncoderExample1)
   基于GLOVE训练得到的英文Embedding模型，词向量的纬度为50，词表大小为400002，
   训练采用的语料是——Wiki2014 + GigaWord英文文本数据。
  
-  glove_wiki2014_gigaword_dim100 (WordEncoderExample2)
   基于GLOVE训练得到的英文Embedding模型，词向量的纬度为100，词表大小为400002，
   训练采用的语料是——Wiki2014 + GigaWord英文文本数据。
   
-  glove_wiki2014_gigaword_dim300 (WordEncoderExample3)
   基于GLOVE训练得到的英文Embedding模型，词向量的纬度为300，词表大小为400002，
   训练采用的语料是——Wiki2014 + GigaWord英文文本数据。
  
-  glove_twitter_dim50 (WordEncoderExample4)
   基于GLOVE训练得到的英文Embedding模型，词向量的纬度为50，词表大小为1193516，
   训练采用的语料是——Twitter英文文本数据。

-  glove_twitter_dim100 (WordEncoderExample5)
   基于GLOVE训练得到的英文Embedding模型，词向量的纬度为50，词表大小为1193516，
   训练采用的语料是——Twitter英文文本数据。
      
### SDK功能：
- 词向量提取
- 相似度计算:
-   余弦相似度
-   内积
 
### 运行例子 - WordEncoderExample1
运行成功后，命令行应该看到下面的信息:
```text
...
[INFO ] - china: [-0.22427, 0.27427, ..., 0.45374, -0.85646]
[INFO ] - america: [-0.22427, 0.27427, ..., 0.45374, -0.85646]
[INFO ] - 余弦相似度: 0.6434219
[INFO ] - 内积: 20.194983
```
### 运行例子 - WordEncoderExample2
运行成功后，命令行应该看到下面的信息:
```text
...
[INFO ] - china: [-0.22427, 0.27427, ..., 0.45374, -0.85646]
[INFO ] - america: [-0.22427, 0.27427, ..., 0.45374, -0.85646]
[INFO ] - 余弦相似度: 0.6434219
[INFO ] - 内积: 20.194983
```
### 运行例子 - WordEncoderExample3
运行成功后，命令行应该看到下面的信息:
```text
...
[INFO ] - china: [-0.11286, 0.033802, ..., 0.47543, -0.13274]
[INFO ] - america: [-0.11286, 0.033802, ..., 0.47543, -0.13274]
[INFO ] - 余弦相似度: 0.33554545
[INFO ] - 内积: 15.388843
```
### 运行例子 - WordEncoderExample4
运行成功后，命令行应该看到下面的信息:
```text
...
[INFO ] - china: [-6.2065E-4, 0.85125, ..., -0.69626, 0.71331]
[INFO ] - america: [-6.2065E-4, 0.85125, ..., -0.69626, 0.71331]
[INFO ] - 余弦相似度: 0.7664755
[INFO ] - 内积: 20.657845
```

### 运行例子 - WordEncoderExample5
运行成功后，命令行应该看到下面的信息:
```text
...
[INFO ] - china: [0.2879, -0.22388, ..., -0.39196, -0.27863]
[INFO ] - america: [0.2879, -0.22388, ..., -0.39196, -0.27863]
[INFO ] - 余弦相似度: 0.6482242
[INFO ] - 内积: 20.837606
```

### 帮助 
-  添加依赖库：lib/aias-word-encoder-en-lib-0.1.0.jar
-  下载对应模型特征数据，添加到 src/test/resources/ 路径下：
-  WordEncoderExample1~3:
-  [glove_wiki2014_gigaword_vocab](https://djl-model.oss-cn-hongkong.aliyuncs.com/models/nlp_models/glove_wiki2014_gigaword_vocab.txt) 
-  WordEncoderExample1:
-  [glove_wiki2014_gigaword_dim50](https://djl-model.oss-cn-hongkong.aliyuncs.com/models/nlp_models/glove_wiki2014_gigaword_dim50.npy)
-  WordEncoderExample2:
-  [glove_wiki2014_gigaword_dim100](https://djl-model.oss-cn-hongkong.aliyuncs.com/models/nlp_models/glove_wiki2014_gigaword_dim100.npy)  
-  WordEncoderExample3:
-  [glove_wiki2014_gigaword_dim300](https://djl-model.oss-cn-hongkong.aliyuncs.com/models/nlp_models/glove_wiki2014_gigaword_dim300.npy)  

-  WordEncoderExample4~5:
-  [glove_twitter_vocab](https://djl-model.oss-cn-hongkong.aliyuncs.com/models/nlp_models/glove_twitter_vocab.txt)  
-  WordEncoderExample4:
-  [glove_twitter_dim50](https://djl-model.oss-cn-hongkong.aliyuncs.com/models/nlp_models/glove_twitter_dim50.npy)  
-  WordEncoderExample5:
-  [glove_twitter_dim100](https://djl-model.oss-cn-hongkong.aliyuncs.com/models/nlp_models/glove_twitter_dim100.npy)  