
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.codehaus.janino.Java;
import scala.Tuple2;

import java.util.Arrays;

public class JavaSparkDemo {
    public static void main(String[] args) {
        /**
         * 编写spark应用程序
         * 第一步，创建sparkconf对象，设置spark应用的配置信息
         * 使用setMaster可以设置spark程序要连接的spark集群的master节点的url
         * 设置为local，则代表本地运行
         */
        SparkConf conf = new SparkConf()
                .setMaster("local[2]")
                .setAppName("WordCountLocalJava");

        /**
         * 第二步，创建JavaContext对象
         * 在spark中，SparkContext是spark所有功能的一个入口，无论是用java，scala还是python
         * 都必须要有一个sparkcontext，它的主要作用包括初始化spark应用程序所需要的一些组件，
         * 包括调度器(DAGScheduler,TaskScheduler)，还会去spark master节点上注册等
         * 在spark中，编写不同的spark应用程序，使用的sparkcontext是不同的，如果使用的是scala，
         * 则是原生的sparkcontext对象，如果使用java，是javaContext对象等
         */
        JavaSparkContext jsc = new JavaSparkContext(conf);

        /**
         * 第三步：要针对输入源(HDFS文件，本地文件等)，创建初始的RDD
         * 输入源中的数据会打散，分配到RDD的每个Partition，从而形成一个初始的分布式数据集
         * sparkcontext中，用于根据文件类型的输入源创建RDD的方法，叫做textfile()方法
         * 在java中创建的普通RDD，都叫做JavaRDD
         */
        JavaRDD<String> lines = jsc.textFile("/Users/packy/Downloads/test.txt");

        /**
         * 第四步：对初始RDD进行transformation操作
         * 通常操作会通过创建function，并配合RDD的map、flatmap等算子来执行
         * function如果比较简单，则创建指定function的匿名内部类
         * 如果比较复杂，则会单独创建一个类，作为实现这个function接口的类
         * 先将每一行拆分为单个的单词
         * FlatMapFunction有两个泛型参数，分别代表了输入和输出
         * 这里输入肯定是String，代表一行一行的文本，输出也是String，
         * FlatMap算子的作用，是将RDD的每一个元素拆分成一个或者多个元素
         */

        /**
         * 在IDEA中使用JDK8的Lamda expression时，要注意调整language level
         * 1、File --> Project Stucture ，在Project Structure中分别在project和model模块选择项目设置Lanugage level 8
         * 2、File --> Settings --> Compiler --> Java Compiler设置Project bytecode version；
         *   同时修改项目对应的Target bytecode version，确保配置的JDK的版本是1.8及以上
         *
         * JDK8的Lamda expression使用起来和Scala的匿名函数非常相似
         */

        JavaRDD<String> words = (JavaRDD<String>) lines.flatMap( s -> Arrays.asList(s.split(" ")).iterator());

        // 在Java中JavaPairRDD和mapToPair配合使用，构造键值对，这里使用了Scala的Tuple2数据类型
        JavaPairRDD<String,Integer> wordpair = (JavaPairRDD<String,Integer>) words.mapToPair(word -> new Tuple2<String,Integer>(word,1));

        JavaPairRDD<String,Integer> count = wordpair.reduceByKey((x,y) -> x+y);

        count.foreach(x -> System.out.println(x));

        jsc.close();
    }
}