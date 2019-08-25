package com.test.gensc.jc.data_ingester;

import com.test.gensc.jc.data_creation.SensorData;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.sql.*;

public class SensorDataProcessor {

    public void processSensorData(){

        Logger.getLogger("org.apache").setLevel(Level.WARN);

        SparkSession spark = SparkSession.builder().appName("testingSql").master("local[*]")
                .getOrCreate();

        // Java Bean (data class) used to apply schema to JSON data
        Encoder<SensorData> sensorDataEncoder = Encoders.bean(SensorData.class);

        String jsonPath = "src/test/testData/sensorDataJsonFile0";

        Dataset<SensorData> ds = spark.read().json(jsonPath).as(sensorDataEncoder);
        ds.show();

        long numberOfRows = ds.count();
        System.out.println("There are " + numberOfRows + " records.");


        SensorData firstRow = ds.first();
        System.out.println("First row id = " + firstRow.getId());
        long id = firstRow.getId();
        double temperatureF = firstRow.getTemperature_f();
        System.out.println(" Id = " + id + " temperature F = " + temperatureF);
        double temperatureC = ((firstRow.getTemperature_f()) - 32 * 5 / 9);
        System.out.println(" Id = " + id + " temperature F = " + temperatureF + " temperature C = " + temperatureC);


        // =========================

        Dataset<Row> newDs = ds.withColumn("temperature_f", ds.col("content.temperature_f"));
        newDs = newDs.drop("content");
        newDs = newDs.withColumn("temperature_c", newDs.col("temperature_f").minus(32).multiply(5).divide(9));



        newDs.show();

        Dataset<FlatSensorData> fds = newDs.as(Encoders.bean(FlatSensorData.class));
        fds.show();

        // =========================



        System.out.println("Got Here!");
        spark.close();
    }
}
