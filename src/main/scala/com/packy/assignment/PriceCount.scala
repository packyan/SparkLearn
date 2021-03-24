package com.packy.assignment

import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @Author: DengAn
 * @Description:
 * @Date: Create in 5:43 下午 2021/3/24 
 */
object PriceCount {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR);
    val sparkConf = new SparkConf().setMaster("local").setAppName("Spark Demo")
    val sparkContext = new SparkContext(sparkConf)
    val data = sparkContext.textFile("/Users/packy/IdeaProjects/SparkStudy/input.dat")
    data.foreach(println(_))
    data.map(line => {
      val ele = line.split(" ")
      var sum=0.0
      ele.takeRight(ele.length-1).map(sum+=_.toFloat)
      (ele(0), sum)
    }).foreach(println(_))
  }

}
