package scala

import org.apache.spark.{SparkConf, SparkContext}

object TestScala {
  def main(args: Array[String]): Unit= {
    val conf = new SparkConf().setAppName("ScaleAppName").setMaster("local[5]");
    val sparkCtx = new SparkContext(conf)
    val data = Array(1, 2, 3, 4, 5, 6, 7)
    val inputRdd = sparkCtx.parallelize(data)
    val count = inputRdd.map(x => {
      x * x
    })
    count.foreach(println(_))
  }

}
