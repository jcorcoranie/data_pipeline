package com.gensc.jc.databases;

import java.sql.*;

public class JdbcTest {

    public static void main(String[] args) throws SQLException {

        Connection myConn = null;
        Statement myStmt = null;
        ResultSet myRs = null;

        try {

            myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sensor_data?useSSL=false", "student", "student");

            System.out.println("Cool it works!");

            myStmt = myConn.createStatement();

            myRs = myStmt.executeQuery("select * from temperatures_data");

            while (myRs.next()){
                System.out.println(myRs.getString("id") + ", " +  myRs.getString("type"));
            }

        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        } finally {
            myConn.close();
        }

    }
}
