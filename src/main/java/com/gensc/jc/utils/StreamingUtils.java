package com.gensc.jc.utils;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class StreamingUtils {

    public JavaStreamingContext getJavaStreamingContext() {
        SparkConf conf = new SparkConf().setAppName("SensorDbLoad").setMaster("local[*]");
        return new JavaStreamingContext(conf, Durations.seconds(1));
    }


    public JavaInputDStream<ConsumerRecord<String, String>> getConsumerRecordInputDStream(JavaStreamingContext sc) {
        Collection topics = Arrays.asList("temperaturerecords");

        Map<String, Object> params = new HashMap<>();
        params.put("bootstrap.servers", "localhost:9092");
        params.put("key.deserializer", StringDeserializer.class);
        params.put("value.deserializer", StringDeserializer.class);
        params.put("group.id", "spark-group");
        params.put("auto.offset.reset", "latest");

        return KafkaUtils.createDirectStream(sc, LocationStrategies.PreferConsistent(),
                ConsumerStrategies.Subscribe(topics, params));
    }
}
