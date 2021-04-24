package com.packy.assignment

import org.apache.log4j.{Level, Logger}
import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.mllib.feature.StandardScaler
import org.apache.spark.mllib.linalg.distributed.RowMatrix

/**
 * @Author: DengAn
 * @Description:
 * @Date: Create in 10:35 上午 2021/3/29 
 */
object ClassificationSample {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR);
    import org.apache.spark.rdd.RDD
    import org.apache.spark.SparkConf
    import org.apache.spark.SparkContext

    val conf = new SparkConf().setAppName("Spark Cls").setMaster("local")
    val sc = new SparkContext(conf)

    val rawInputData = sc.textFile("/Users/packy/Downloads/stumbleupon/train.csv").zipWithIndex()
    val rawData = rawInputData.filter(_._2>0)
    val records:RDD[Array[String]] = {
      rawData.map(_._1.split("\t"))
    }.repartition(9)
    println(records.first()(0))

    import org.apache.spark.mllib.regression.LabeledPoint
    import org.apache.spark.mllib.linalg.Vectors
    val data = records.map{
      r=>
        val trimmed = r.map(_.replaceAll("\"",""))
        val label = trimmed(r.size - 1).toInt
        val feature = trimmed.slice(4,r.size-1).map(d => if (d == "?") 0.0 else d.toDouble)
        LabeledPoint(label, features = Vectors.dense(feature))
    }
    data.cache()
    val numData = data.count()
    println(numData)
    println(data.first().features(1))

    import org.apache.spark.mllib.classification.LogisticRegressionWithLBFGS
    import org.apache.spark.mllib.tree.configuration.Algo
    val numInteration = 15
    val lsModel = org.apache.spark.mllib.classification.LogisticRegressionWithSGD.train(data, numInteration)
    val dataPoint = data.first()
    val prediciton = lsModel.predict(dataPoint.features)
    val truelabel = dataPoint.label
    println("prediciton: " + prediciton + " truelabel: " + truelabel)

    val predictions = lsModel.predict(data.map(lp=>lp.features))
    println(predictions.take(5))

    // comput acc
    val lrTotalCorrect = data.map{
      point =>
        if(lsModel.predict(point.features) == point.label) 1 else 0
    }.sum()

    println("train acc : " + lrTotalCorrect/numData)

    //官方ROC PR 计算
    import org.apache.spark.mllib.evaluation.BinaryClassificationMetrics
    val metrics = Seq(lsModel).map{ model =>
      val scoreAndLabels = data.map {
        point => (model.predict(point.features), point.label)
      }
      val metrics = new BinaryClassificationMetrics(scoreAndLabels=scoreAndLabels)
          (model.getClass.getSimpleName, metrics.areaUnderPR(), metrics.areaUnderROC())

    }
    metrics.foreach(println(_))

    //观察特征的最小值 最大值 方差等
    val vectors = data.map(lp => lp.features)
    val matrix = new RowMatrix(vectors)
    val matrixSummary = matrix.computeColumnSummaryStatistics()
    println(matrixSummary.mean)
    println(matrixSummary.max)
    println(matrixSummary.min)
    println(matrixSummary.variance)
    println(matrixSummary.numNonzeros)

    //对特征进行归一化后计算

    val scaler = new StandardScaler(withMean = true, withStd = true).fit(vectors);
    val scaledData = data.map(lp=> LabeledPoint(lp.label, scaler.transform(lp.features)))
    println(data.first().features)
    println(scaledData.first().features)



    val lrModelScaled = org.apache.spark.mllib.classification.LogisticRegressionWithSGD.train(scaledData, numInteration)
    // comput acc
    val lrTotalCorrect2 = scaledData.map{
      point =>
        if(lsModel.predict(point.features) == point.label) 1 else 0
    }.sum()
    println("train acc : " + lrTotalCorrect2/numData)

  }

}
