package com.test.gensc.jc;

import com.test.gensc.jc.data_creation.CreateTestData;
import com.test.gensc.jc.data_ingester.SensorDataProcessor;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Starting App run!" );
        Logger.getLogger("org.apache").setLevel(Level.WARN);

        CreateTestData createTestData = new CreateTestData();
        createTestData.createTestData();

        SensorDataProcessor sensorDataProcessor = new SensorDataProcessor();
        sensorDataProcessor.processSensorData();



        System.out.println( "Finished App run!" );

    }
}
