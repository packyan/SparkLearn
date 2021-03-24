package com.packy.spark

/**
 * @Author: DengAn
 * @Description:
 * @Date: Create in 5:25 下午 2021/3/24 
 */

import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import org.apache.log4j.{Level,Logger}
object Others {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR);
    val sparkConf = new SparkConf().setAppName("SparkApp").setMaster("local")
    val sparkContext = new SparkContext(sparkConf)
    val data = Array(1, 2, 3, 4, 5,500,100,23,24,54,65)
    val inputRDD = sparkContext.parallelize(data)
    //抽样函数，是否有放回，无放回，抽样比例等,随机数种子等
    inputRDD.sample(false,0.5,10).foreach(println(_))

    //笛卡尔积，两两组合，数据量爆炸
    inputRDD.map(_*7).cartesian(inputRDD).foreach(println(_))

    //管道 取出若干条数据？
    //inputRDD.map(_*7).cartesian(inputRDD).pipe("head -1").foreach(println(_))

    //action操作
    inputRDD.map(_*7).cartesian(inputRDD).collect().foreach(println(_))

    inputRDD.map(_*7).cartesian(inputRDD).first()
    inputRDD.map(_*7).cartesian(inputRDD).take(3).foreach(println(_))

  }
}