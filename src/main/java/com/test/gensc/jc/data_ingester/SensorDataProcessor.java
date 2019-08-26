package com.test.gensc.jc.data_ingester;

import com.test.gensc.jc.data_creation.SensorData;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.api.java.function.ForeachPartitionFunction;
import org.apache.spark.sql.*;

import java.io.Serializable;
import java.util.Properties;

public class SensorDataProcessor implements Serializable {

    public void processSensorData(){

        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("acks", "all"); // See https://kafka.apache.org/documentation/
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Logger.getLogger("org.apache").setLevel(Level.WARN);

        SparkSession spark = SparkSession.builder().appName("testingSql").master("local[*]")
                .getOrCreate();

        // Java Bean (data class) used to apply schema to JSON data
        Encoder<SensorData> sensorDataEncoder = Encoders.bean(SensorData.class);

        String jsonPath = "src/test/testData/sensorDataJsonFile*";

        Dataset<SensorData> ds = spark.read().json(jsonPath).as(sensorDataEncoder);


        // =========================

        Dataset<Row> newDs = ds.withColumn("temperature_f", ds.col("content.temperature_f"));
        newDs = newDs.drop("content");
        newDs = newDs.withColumn("temperature_c", newDs.col("temperature_f").minus(32).multiply(5).divide(9));


        newDs.foreachPartition((ForeachPartitionFunction<Row>) t -> {
            Producer<String, String> producer = new KafkaProducer<>(props);
            while (t.hasNext()){

                Row row = t.next();
                System.out.println(row.toString());
                producer.send(new ProducerRecord<>("viewrecords", row.toString()));
            }
            producer.close();
        });

        spark.close();
    }
}
