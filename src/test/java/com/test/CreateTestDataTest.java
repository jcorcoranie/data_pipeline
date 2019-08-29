package com.test;

import com.gensc.jc.data_creation.CreateTestData;
import com.gensc.jc.domain.SensorData;
import com.gensc.jc.utils.Utils;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;

public class CreateTestDataTest {

    @Test
    public void createTestData() {

        Properties properties = new Utils().invoke();
        CreateTestData createTestData = new CreateTestData();

        List<SensorData> sensorData = new ArrayList<>();
        String fileName = properties.getProperty("json.FileName");
        String fileDir = properties.getProperty("json.FileDir");
        int fileLimit = Integer.valueOf(properties.getProperty("file.limit"));
        int recordLimit = Integer.valueOf(properties.getProperty("record.limit"));

        List<String> results = createTestData.createTestData();

        boolean check = false;
        for(String name: results){

            check = new File(fileDir, name).exists();
            System.out.println(name + " ---- Check =" + check);
            assertThat(check, is(true));
        }


    }
}