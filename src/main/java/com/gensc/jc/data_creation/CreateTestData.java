package com.gensc.jc.data_creation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gensc.jc.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class CreateTestData {

    private SensorData sensorRecord;
    private List<SensorData> sensorData;

    private static SimpleDateFormat sdf = null;
    ObjectMapper mapper = new ObjectMapper();



    public List<String> createTestData() {

        Properties properties = new Utils().invoke();
        sdf = new SimpleDateFormat(properties.getProperty("isoDateTimeFormat"));

        int randomId;
        int fileLimit = Integer.valueOf(properties.getProperty("file.limit"));
        int recordLimit = Integer.valueOf(properties.getProperty("record.limit"));
        double randomTempF;
        List<String> fileNames = new ArrayList<>();

        for (int f = 0; f < fileLimit; f++) {

            sensorData = new ArrayList<>();
            String fileName = properties.getProperty("json.FileName") + f;
            fileNames.add(fileName);

            for (int i = 0; i < recordLimit; i++) {

                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                String isoDateTime = sdf.format(timestamp);

                randomId = (int) (Math.random() * ((1000 - 1) + 1)) + 1;  // assume there are 1000 sensors.
                randomTempF = (Math.random() * ((150 - 1) + 1)) + 1;        // assume temperature range 150 degrees F.

                sensorRecord = new SensorData(randomId, properties.getProperty("json.File.Type"), randomTempF, isoDateTime);
                sensorData.add(sensorRecord);
            }

            try {

                mapper.writeValue(new File(properties.getProperty("json.FileDir") + fileName), sensorData);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return fileNames;
    }
}

