package com.gensc.jc.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Utils {
    public Properties invoke() {
        Properties properties = null;
        try (InputStream input = new FileInputStream("resources/application.properties")) {

            properties = new Properties();
            properties.load(input);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return properties;
    }
}
