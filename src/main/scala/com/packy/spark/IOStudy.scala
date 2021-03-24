package com.packy.spark

/**
 * @Author: DengAn
 * @Description:
 * @Date: Create in 5:32 下午 2021/3/24 
 */
import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import org.apache.log4j.{Level,Logger}
object IOStudy {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR);
    val sparkConf = new SparkConf().setAppName("SparkApp").setMaster("local")
    val sparkContext = new SparkContext(sparkConf)
    //读取可以文件，也可以目录
    val inputRdd = sparkContext.textFile("/Users/packy/IdeaProjects/SparkStudy/input.dat")
    inputRdd.foreach(println(_))
    inputRdd.map(_.length).saveAsTextFile("/Users/packy/IdeaProjects/SparkStudy/output.txt")
  }
}