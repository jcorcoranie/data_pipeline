package com.gensc.jc.data_creation;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gensc.jc.domain.SensorData;
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

            } catch (NullPointerException e) {
                System.err.println("A NullPointerException has been caught. The file name and or path may be empty!");
                e.printStackTrace();
            } catch (JsonMappingException e) {
                System.err.println("A JsonMappingException has been caught. Failed to convert the Sensor data to JSON format!");
                e.printStackTrace();
            } catch (JsonGenerationException e) {
                System.err.println("A JsonGenerationException has been caught. Failed to generate the JSON file of sensor data!");
                e.printStackTrace();
            } catch (IOException e) {
                System.err.println("A IOException has been caught. Failed to write the Sensor data JSON file to testData directory!");
                e.printStackTrace();
            }
        }

        return fileNames;
    }
}

