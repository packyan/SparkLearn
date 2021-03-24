package com.packy.spark

/**
 * @Author: DengAn
 * @Description:
 * @Date: Create in 4:44 下午 2021/3/24 
 */
import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import org.apache.log4j.{Level,Logger}
object UnionStudy {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR);
    val sparkConf = new SparkConf().setAppName("SparkApp").setMaster("local")
    val sparkContext = new SparkContext(sparkConf)
    val data = Array(1,2,3,4,5)
    val dataArr = Array(Array(1,2,3),Array(23,4,21))

    println("union Demo")
    val inputRdd1 = sparkContext.parallelize(data)
    val inputRdd2 = inputRdd1.map(x => x*2)
    val inputRdd3 = inputRdd1.union(inputRdd2).foreach(println(_))
    println("join Demo")
    val joinLeft = inputRdd1.map(x => (x,1))
    var joinRight = inputRdd2.map((_, 2))
    joinLeft.join(joinRight).foreach(println(_))
    println("intersection Demo")
    joinRight = inputRdd2.map((_,1))
    joinLeft.intersection(joinRight).foreach(println(_))
    println("cogroup Demo")
    joinLeft.cogroup(joinRight).foreach(println(_))

  }
}
