package scala
import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import org.apache.log4j.{Level,Logger}
object SparkTansformationAPI {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR);
    val sparkConf = new SparkConf().setAppName("SparkApp").setMaster("local")
    val sparkContext = new SparkContext(sparkConf)
    val data = Array(1,2,3,4,5)
    val inputRdd = sparkContext.parallelize(data)
    inputRdd.map({x => x*x*x}).filter(x => x > 20).collect().foreach(println(_))

    val dataArr = Array(Array(1,3,4),Array(23,4,21))
    val inputRdd2 = sparkContext.parallelize(dataArr)
    inputRdd2.flatMap(x =>{x.map(y => y*2)}).collect().foreach(println(_))
  }
}
