package com.gensc.jc.data_creation;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class GetData {

    ObjectMapper mapper = new ObjectMapper();
    SensorData data;

    public void getData(){
        try {
            data = mapper.readValue(new File("src/test/testData/sensorDataEx1.json"), SensorData.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void displayData(){

        System.out.println("Data : \n" + data.toString());
    }

}
