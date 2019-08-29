package com.gensc.jc.utils;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import scala.Tuple5;

import java.io.Serializable;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.*;

public class DatabaseUtils implements Serializable {

    public void sensorDataLoadToDb(Properties properties, JavaDStream<Tuple5<String, String, String, String, String>> sensorDataStringTuple){

        sensorDataStringTuple.foreachRDD(rdd -> {

            if(!rdd.isEmpty()){
                rdd.foreachPartition(partitionOfRecords -> {

                    Connection myConn= null;
                    Statement myStmt=null;
                    try {
                        myConn = DriverManager.getConnection(properties.getProperty("db_conn"), properties.getProperty("db_user"), properties.getProperty("db_pass"));
                        myStmt= myConn.createStatement();
                    } catch (SQLException se){
                        System.err.println("A SQLException has been caught. The Database connection has failed!");
                        se.printStackTrace();
                        throw se;
                    }

                    while (partitionOfRecords.hasNext()) {
                        String insertStr = getUpdateString(partitionOfRecords);

                        try {
                            myStmt.executeUpdate(insertStr);
                        } catch (SQLException se) {
                            System.err.println("A SQLException has been caught. The failed to close the database connection");
                            se.printStackTrace();
                        }

                    }

                    try {
                        if(!myConn.isClosed()){
                            myConn.close();
                        }
                    } catch (SQLException se) {
                        System.err.println("A SQLException has been caught. The failed to close the database connection");
                        se.printStackTrace();
                    }
                });
            }
        });
    }


    public String getUpdateString(Iterator<Tuple5<String, String, String, String, String>> partitionOfRecords) {
        Tuple5<String,String,String,String,String> tuple = partitionOfRecords.next();

        DecimalFormat decimalFormat = new DecimalFormat("###.##");
        decimalFormat.setRoundingMode(RoundingMode.CEILING);

        return "insert into temperatures_data (id, type, temperaturef, temperaturec, timeOfMeasurement) " +
                "values(" + tuple._1() + ", '" +
                tuple._2() + "', " +
                decimalFormat.format(Double.valueOf(tuple._3())) + ", " +
                decimalFormat.format(Double.valueOf(tuple._4())) + ", '" +
                tuple._5() + "')";
    }


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
