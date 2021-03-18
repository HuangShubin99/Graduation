package com.scStream.sparkservice;

import java.util.*;
import kafka.serializer.StringDecoder;
import lombok.extern.slf4j.Slf4j;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaPairInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.*;
//import org.apache.spark.streaming.kafka.KafkaRDDPartition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SparkMain {

  // ${property: default_value}: 注入外部参数对应的property
  // #{obj.property: default_value}: SpEL表达式对应的内容
  @Value("${like.topic}")
  private String topic;

  public void launch() {
    SparkConf conf = new SparkConf().setAppName("StreamProcess")
          .setMaster("local[4]")
          .set("spark.streaming.kafka.maxRatePerPartition", "5");

    Set<String> topics = Collections.singleton(topic);  // Collections.singleton()返回单元素集合

    Map<String, String> kafkaParams = new HashMap<>();
    kafkaParams.put("metadata.broker.list", "node1:9092,node2:9092,node3:9092,node4:9092,node5:9092,node6:9092,node7:9092,node8:9092,node9:9092,node10:9092");


    JavaStreamingContext jsc = new JavaStreamingContext(
          new JavaSparkContext(conf),
          Durations.seconds(3));

    /**
     * checkpoint机制
     * 1. Metadata checkpoint: 将流式计算的信息保存到具有容错性的存储
     *   元数据包括：
     *   1. Configuration(配置信息): 创建streaming应用程序的配置信息
     *   2. DStream operations: 在streaming应用程序中定义的DStreaming操作
     *   3. Incomplete batches: 在队列中没有处理完的作业
     * 2. Data checkpoint
     */
    jsc.checkpoint("checkpoint");

    // 得到数据流
    final JavaPairInputDStream<String, String> stream = KafkaUtils
          .createDirectStream(
            jsc,
            String.class,
            String.class,
            StringDecoder.class,
            StringDecoder.class,
            kafkaParams,
            topics
          );
    System.out.println("stream started!");

    stream.print();

    jsc.start();
  }

}