package com.test.gensc.jc.databases;

import java.sql.*;

public class JdbcCrud {

    public int insertTemperatureSensorData(int id, String dataType, Double Tempf, Double Tempc) throws SQLException {

        Connection myConn = null;
        Statement myStmt = null;
        ResultSet myRs = null;
        int numberOfRows = 0;

        try {

            myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sensor_data?useSSL=false", "student", "student");
            myStmt = myConn.createStatement();

            String insertStr = "insert into temperatures_data (id, type, temperaturef, temperaturec) values(" + id + ", '" + dataType + "', " + Tempf + ", " + Tempc + ")";
            System.out.println("insertStr = " + insertStr);

            numberOfRows = myStmt.executeUpdate(insertStr);



        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        } finally {
            myConn.close();
        }

        return numberOfRows;

    }
}
