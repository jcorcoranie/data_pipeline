package com.gensc.jc.data_ingester;

import com.gensc.jc.domain.SensorData;
import com.gensc.jc.utils.Utils;
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

    private static final Properties properties = new Utils().invoke();
    private static final String devMode = properties.getProperty("dev.mode");


    public static void main(String[] args){

        Logger.getLogger("org.apache").setLevel(Level.WARN);

        SensorDataProcessor sensorDataProcessor = new SensorDataProcessor();
        sensorDataProcessor.processSensorData();
    }

    public void processSensorData(){

        // Read in Json file and create dataset
        SparkSession spark = SparkSession.builder().appName("SensorDataPipeline").master("local[*]").getOrCreate();
        Dataset<SensorData> sensorDataSet = getSensorDataDataset(spark);

        // Flatten data set removing the content. Calculate and add the temperature in C.
        Dataset<Row> extendedSensorDataSet = getRowDataset(sensorDataSet);

        // put the data into the pipeline.
        putDataOnPipeline(extendedSensorDataSet);

        spark.close();
    }

    private void putDataOnPipeline(Dataset<Row> extendedSensorDataSet) {

        Properties props = getKafkaProperties();

        extendedSensorDataSet.foreachPartition((ForeachPartitionFunction<Row>) t -> {
            Producer<String, String> producer = new KafkaProducer<>(props);
            while (t.hasNext()){

                Row row = t.next();
                if(devMode.equals("true")) System.out.println(row.toString());
                producer.send(new ProducerRecord<>("temperaturerecords", row.toString()));
            }
            producer.close();
        });
    }

    private Dataset<SensorData> getSensorDataDataset(SparkSession spark) {

        // Java Bean (data class) used to apply schema to JSON data
        Encoder<SensorData> sensorDataEncoder = Encoders.bean(SensorData.class);

        String jsonPath = properties.getProperty("json.FileDir") + properties.getProperty("json.FileName") + "*";

        return spark.read().json(jsonPath).as(sensorDataEncoder);

    }

    private Properties getKafkaProperties() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        return props;
    }

    private Dataset<Row> getRowDataset(Dataset<SensorData> ds) {
        Dataset<Row> newDs = ds.withColumn("temperature_f", ds.col("content.temperature_f"));
        newDs = newDs.withColumn("temperature_c", newDs.col("temperature_f").minus(32).multiply(5).divide(9));
        newDs = newDs.withColumn("time_of_measurement", ds.col("content.time_of_measurement"));
        newDs = newDs.drop("content");
        return newDs;
    }
}
