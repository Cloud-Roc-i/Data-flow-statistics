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
4. python目录里是模拟产生日志的脚本，flume目录里的是flume的配置文件

# 2021.1.20改动
* 新增spark_web目录，该目录是一个完整的springboot项目，需要单独运行。主要功能是读取hbase中的数据进行可视化并在web上展示。
* 页面端口和路径可查看src/main/resources/application.properties文件
* HTML代码用的是echart官方实例饼图，如需换成其他图表，可访问echart官网：https://echarts.apache.org/
