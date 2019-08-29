package com.gensc.jc.pipeline_ingester;

import com.gensc.jc.utils.DatabaseUtils;
import com.gensc.jc.utils.Utils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import scala.Tuple5;

import java.io.Serializable;
import java.util.*;

public class TemperatureSensorDataDStreamProcess implements Serializable {

    private static final Properties properties = new Utils().invoke();
    private static final String devMode = properties.getProperty("dev.mode");

    //throws InterruptedException
    public static void main(String[] args)  {

        Logger.getLogger("org.apache").setLevel(Level.WARN);
        Logger.getLogger("org.apache.spark.storage").setLevel(Level.ERROR);
        DatabaseUtils databaseUtils = new DatabaseUtils();


        JavaStreamingContext sc = databaseUtils.getJavaStreamingContext();

        JavaInputDStream<ConsumerRecord<String, String>> consumerRecordInputDStream = databaseUtils.getConsumerRecordInputDStream(sc);

        JavaDStream<String> sensorDataDStream = consumerRecordInputDStream.map(record -> record.value());

        if(devMode.equals("true")) sensorDataDStream.print();

        JavaDStream<Tuple5<String, String, String, String, String>> sensorDataStringTuple = sensorDataDStream.map(data -> new Tuple5<>(data.split(",")[0].replace("[", ""), data.split(",")[1], data.split(",")[2], data.split(",")[3], data.split(",")[4].replace("]", "")));

        databaseUtils.sensorDataLoadToDb(properties, sensorDataStringTuple);



        sc.start();

        try {
            sc.awaitTermination();
        } catch (InterruptedException e) {
            System.err.println("A InterruptedException has been caught. The awaitTermination method has been interrupted and failed to await!");
            e.printStackTrace();
        }
    }

}

