package com.packy.spark

/**
 * @Author: DengAn
 * @Description:
 * @Date: Create in 4:58 下午 2021/3/24 
 */
import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import org.apache.log4j.{Level,Logger}
object PartitionStudy {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR);
    val sparkConf = new SparkConf().setAppName("SparkApp").setMaster("local")
    val sparkContext = new SparkContext(sparkConf)
    val data = Array(1, 2, 3, 4, 5,6,7,8,9,10,11,12,13,14,15)
    // 把大数据拆分numSlices小块
    val inputRdd = sparkContext.parallelize(data, numSlices = 3)
    //和直接map没有区别
    inputRdd.mapPartitions(
      //database connection
      x => {
        // database insert
        x.map(y =>
        {y*2}
        )}
    ).foreach(println(_))
    //获取partition的index
    inputRdd.mapPartitionsWithIndex(
      (idx, iter)=>{
        iter.map(
          x => (x, idx)
        )
      }
    ).foreach(println(_))


  //重新partition, 性能消耗操作
    inputRdd.repartition(5).mapPartitionsWithIndex(
      (idx, iter)=>{
        iter.map(
          x => (x, idx)
        )
      }
    ).foreach(println(_))
//减少partition，shuffle操作
    inputRdd.coalesce(2,false).mapPartitionsWithIndex(
      (idx, iter)=>{
        iter.map(
          x => (x, idx)
        )
      }
      ).foreach(println(_))
  }
}