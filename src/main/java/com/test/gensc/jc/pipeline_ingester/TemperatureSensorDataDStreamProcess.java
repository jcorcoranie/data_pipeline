package com.test.gensc.jc.pipeline_ingester;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import scala.Tuple4;

import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TemperatureSensorDataDStreamProcess {


    public static void main(String[] args) throws InterruptedException {

        Logger.getLogger("org.apache").setLevel(Level.WARN);
        Logger.getLogger("org.apache.spark.storage").setLevel(Level.ERROR);

        SparkConf conf = new SparkConf().setAppName("viewingfigures").setMaster("local[*]");
        JavaStreamingContext sc = new JavaStreamingContext(conf, Durations.seconds(1));

        Collection topics = Arrays.asList("temperaturerecords");

        Map<String, Object> params = new HashMap<>();
        params.put("bootstrap.servers", "localhost:9092");
        params.put("key.deserializer", StringDeserializer.class);
        params.put("value.deserializer", StringDeserializer.class);
        params.put("group.id", "spark-group");
        params.put("auto.offset.reset", "latest");


        JavaInputDStream<ConsumerRecord<String, String>> stream = KafkaUtils.createDirectStream(sc, LocationStrategies.PreferConsistent(),
                ConsumerStrategies.Subscribe(topics, params));

        JavaDStream<String> results = stream.map(item -> item.value());

        JavaDStream<Tuple4<String, String, String, String>> dStream = results.map(data -> new Tuple4<>(data.split(",")[0].replace("[", ""), data.split(",")[1], data.split(",")[2], data.split(",")[3].replace("]", "")));

        dStream.foreachRDD(rdd -> {

            if(!rdd.isEmpty()){
                rdd.foreachPartition(partitionOfRecords -> {

                    Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sensor_data?useSSL=false", "student", "student");
                    Statement myStmt = myConn.createStatement();
                    int numberOfRows = 0;
                    DecimalFormat df = new DecimalFormat("###.##");
                    df.setRoundingMode(RoundingMode.CEILING);

                    while (partitionOfRecords.hasNext()) {
                        
                        Tuple4<String,String,String,String> tuple = partitionOfRecords.next();

                        String insertStr = "insert into temperatures_data (id, type, temperaturef, temperaturec) " +
                                                "values(" + tuple._1() + ", '" +
                                                            tuple._2() + "', " +
                                                            df.format(Double.valueOf(tuple._3())) + ", " +
                                                            df.format(Double.valueOf(tuple._4())) + ")";
                        System.out.println(insertStr);
                        numberOfRows = myStmt.executeUpdate(insertStr);

                    }
                    myConn.close();
                });
            }
        });
        sc.start();
        sc.awaitTermination();
    }
}
