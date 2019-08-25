package com.test.gensc.jc.data_creation;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CreateTestData {

    private int id;
    private String type;
    private SensorData sensorRecord;
    private double temperature;
    private Date dateTime;
    private List<SensorData> sensorData;

    private static final int FILE_LIMIT = 10;
    private static final int DATA_LIMIT = 200;
    private static final String FILE_NAME = "sensorDataJsonFile";

    ObjectMapper mapper = new ObjectMapper();


    public void createTestData(){

        int randomId;
        double randomTempF;


        for (int f = 0; f < FILE_LIMIT; f++) {

            sensorData = new ArrayList<>();
            String fileName = FILE_NAME + f;

            for (int i = 0; i < DATA_LIMIT; i++) {


                randomId = (int) (Math.random() * ((1000 - 1) + 1)) + 1;
                randomTempF = (Math.random() * ((150 - 1) + 1)) + 1;

                sensorRecord = new SensorData(randomId, "Sensor", randomTempF);
                sensorData.add(sensorRecord);

            }



            try {
                mapper.writeValue(new File("src/test/testData/" + fileName), sensorData);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    }




}
