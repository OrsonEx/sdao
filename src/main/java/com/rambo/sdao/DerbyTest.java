package com.rambo.sdao;

import java.sql.*;

public class DerbyTest {
    String dbName = "E:\\Users\\Workspaces\\Derby\\dedb";

    public static void loadDriver() {
        try {
            String driver = "org.apache.derby.jdbc.EmbeddedDriver";
            Class.forName(driver).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getDataFromDerby() {
        try {
            String protocol = "jdbc:derby:";
            Connection conn = DriverManager.getConnection(protocol + dbName + ";user=root;password=root;create=true");
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from t_user");
            while (resultSet.next()) {
                System.out.println(resultSet.getString(1));
                System.out.println(resultSet.getString(2));
            }
            conn.close();
            statement.close();
            resultSet.close();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public static void main(String[] args) {
//        DerbyTest derbyTest = new DerbyTest();
//        loadDriver();
//        derbyTest.getDataFromDerby();
        try {
            Connection conn = DB.getConn();
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from t_user");
            while (resultSet.next()) {
                System.out.println(resultSet.getString(1));
                System.out.println(resultSet.getString(2));
            }
            conn.close();
            statement.close();
            resultSet.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}