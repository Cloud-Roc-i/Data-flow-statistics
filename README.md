# Data-flow-statistics
A bigdata project with spark, kafka and flume

# 前言
本项目是一个大数据实时流统计项目，通过Python脚本模拟日志的产生，用flume监听日志文件并提交到Kafka，然后用spark读取Kafka中的数据，进行数据清洗、格式转换等工作之后上传到Hbase。

# 软件版本
```
<scala.version>2.10.7</scala.version>
<kafka.version>0.10.2.0</kafka.version>
<spark.version>2.4.0</spark.version>
<hadoop.version>3.1.3</hadoop.version>
<hbase.version>2.2.2</hbase.version>
```

# 注意事项
1. 需要提前在Hbase里建好表“click_count”和“category_search_count”
2. 要确保各种软件在服务器或虚拟机上都能正常运行
3. Hbase新版本的API有所变化，不能直接调用HbaseAdmin，而是要通过Connection的getAdmin方法进行调用。源码解释如下：
* HBaseAdmin is no longer a client API. It is marked InterfaceAudience.Private indicating that
* this is an HBase-internal class as defined in
* https://hadoop.apache.org/docs/current/hadoop-project-dist/hadoop-common/InterfaceClassification.html
* There are no guarantees for backwards source / binary compatibility and methods or class can
* change or go away without deprecation.
* Use {@link Connection#getAdmin()} to obtain an instance of {@link Admin} instead of constructing
* an HBaseAdmin directly.
