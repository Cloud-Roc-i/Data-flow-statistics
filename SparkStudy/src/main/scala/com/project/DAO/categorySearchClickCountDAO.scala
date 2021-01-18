package com.project.DAO

import com.project.domain.{categoryClickCount, categorySearchClickCount}
import com.study.utils.HBaseUtils
import org.apache.hadoop.hbase.client.Get
import org.apache.hadoop.hbase.util.Bytes

import scala.collection.mutable.ListBuffer

object categorySearchClickCountDAO {

  val tableName = "category_search_count"
  val cf = "info"
  val qualifier = "click_count"

  //保存数据
  def save(list:ListBuffer[categorySearchClickCount]): Unit ={
      val table = HBaseUtils.getInstance().getTable(tableName)
      for(els<-list){
        table.incrementColumnValue(Bytes.toBytes(els.day_search_category),Bytes.toBytes(cf),Bytes.toBytes(qualifier),els.clickCount)
      }
  }

  def count(day_category:String):Long= {
    val table = HBaseUtils.getInstance().getTable(tableName)
    val get = new Get(Bytes.toBytes(day_category))
    val value = table.get(get).getValue(Bytes.toBytes(cf),Bytes.toBytes(qualifier))
    if(value == null){
      0L
    }else {
      Bytes.toLong(value)
    }
  }

  def main(args: Array[String]): Unit = {
    val list = new ListBuffer[categorySearchClickCount]
    list.append(categorySearchClickCount("20171122_1_8",300))
    save(list)
    print(count("20171122_8"))
  }
}
