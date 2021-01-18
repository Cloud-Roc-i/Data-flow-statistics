package com.project

import com.project.DAO.{categoryClickCountDAO, categorySearchClickCountDAO}
import com.project.domain.{ClickLog, categoryClickCount, categorySearchClickCount}
import com.project.util.DateUnits
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe

import scala.collection.mutable.ListBuffer

object SparkStreamingApp {
  def main(args: Array[String]): Unit = {
    val ssc = new StreamingContext("local[*]","SparkStreamingApp",Seconds(5))
    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> "localhost:9092,localhost:9092",
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> "test",
      "auto.offset.reset" -> "latest",
      "enable.auto.commit" -> (false: java.lang.Boolean)
    )
    val topics = Array("flumeTopic")
    val stream = KafkaUtils.createDirectStream[String, String](
      ssc,
      PreferConsistent,
      Subscribe[String, String](topics, kafkaParams)
    ).map(_.value())

    var cleanLog = stream.map(line=>{
      var infos = line.split("\t")
      var url = infos(2).split(" ")(1)
      var categoryID = 0
      if(url.startsWith("www")){
        categoryID = url.split("/")(2).toInt
      }
      ClickLog(infos(0),DateUnits.parseToMin(infos(1)),categoryID,infos(3),infos(4).toInt)
    }).filter(log=>log.categoryID!=0)

    //每个类别每天的点击量(day_category,1)
    cleanLog.map(log=>{
      (log.time.substring(0,8)+log.categoryID,1)
    }).reduceByKey(_+_).foreachRDD(rdd=>{
      rdd.foreachPartition(partitions=>{
        val list = new ListBuffer[categoryClickCount]
        partitions.foreach(pair=>{
          list.append(categoryClickCount(pair._1,pair._2))
        })
        categoryClickCountDAO.save(list)
      })
    })

    //每个栏目下面从渠道过来的流量time_www.baidu.com_5
    cleanLog.map(log=>{
      val url = log.refer.replace("//","/").split("/")
      var host = ""
      if(url.length > 2){
        host = url(1)
      }
      (host,log.time,log.categoryID)
    }).filter(x=>x._1 != "").map(x=>{
      (x._2.substring(0,8) + "_" + x._1 + "_" + x._3 , 1)
    }).reduceByKey(_+_).foreachRDD(rdd=>{
      rdd.foreachPartition(partitions=>{
        val list = new ListBuffer[categorySearchClickCount]
        partitions.foreach(pairs => {
          list.append(categorySearchClickCount(pairs._1,pairs._2))
        })
        categorySearchClickCountDAO.save(list)
      })
    })

    ssc.start()
    ssc.awaitTermination()
  }
}
