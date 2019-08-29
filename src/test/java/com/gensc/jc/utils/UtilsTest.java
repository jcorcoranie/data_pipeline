package com.gensc.jc.utils;

import org.junit.Test;

import java.util.Properties;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

public class UtilsTest {

    @Test
    public void invokeReturnsRequiredFieldsTest() {

        Properties properties = new Utils().invoke();

        assertThat(properties.toString(), containsString("file.limit"));
        assertThat(properties.toString(), containsString("db_conn=jdbc:mysql://"));
        assertThat(properties.toString(), containsString("/sensor_data"));
        assertThat(properties.toString(), containsString("db_user="));
        assertThat(properties.toString(), containsString("record.limit="));
        assertThat(properties.toString(), containsString("json.FileDir="));
        assertThat(properties.toString(), containsString("isoDateTimeFormat=yyyy-MM-dd'T'HH.mm.ss"));
        assertThat(properties.toString(), containsString("db_pass="));
        assertThat(properties.toString(), containsString("json.File.Type=Sensor"));
        assertThat(properties.toString(), containsString("json.FileName=sensorDataJsonFile"));
    }



}