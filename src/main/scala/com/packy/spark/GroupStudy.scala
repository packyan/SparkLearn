package com.packy.spark

/**
 * @Author: DengAn
 * @Description:
 * @Date: Create in 5:12 下午 2021/3/24
 */

import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import org.apache.log4j.{Level,Logger}
object GroupStudy {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR);
    val sparkConf = new SparkConf().setAppName("SparkApp").setMaster("local")
    val sparkContext = new SparkContext(sparkConf)
    val data = Array(Tuple2(1,2), Tuple2(1,4),Tuple2(1,5),Tuple2(2,4),Tuple2(2,3),Tuple2(3,66))
    val inputRdd = sparkContext.parallelize(data)
    //以元组里第一个元素为主要，聚合。
    inputRdd.groupBy(_._1).foreach(println(_))

    //元组前面的值就是key
    inputRdd.groupByKey().foreach(println(_))

    //reduce by key
    inputRdd.reduceByKey((x,y) => x + y).foreach(println(_))
//优先选择ReducebyKey，预聚合，性能高，下面这个版本性能消耗大
    inputRdd.groupByKey().map(x => {
      val key = x._1
      var sum = 0
      x._2.map( y=>{
        sum += y;
      })
      (key, sum)
    }).foreach(println(_))

  }
}