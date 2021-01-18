package com.project.util

import java.text.SimpleDateFormat

import org.apache.commons.lang.time.FastDateFormat
import java.util.Date

object DateUnits {
      val YYYYMMDDHHMMSS_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
      val TAG_FORMAT = FastDateFormat.getInstance("yyyyMMdd")

      //把时间转换成时间戳
      def getTime(time:String) ={
         YYYYMMDDHHMMSS_FORMAT.parse(time).getTime
      }

      def parseToMin(time:String) = {
        TAG_FORMAT.format(new Date(getTime(time)))
      }

  def main(args: Array[String]): Unit = {
    print(parseToMin("2020-1-14 08:25:20"))
  }
}
