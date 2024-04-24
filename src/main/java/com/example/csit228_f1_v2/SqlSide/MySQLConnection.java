package com.example.csit228_f1_v2.SqlSide;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class MySQLConnection {
    public static final String URL = "jdbc:mysql://localhost:3306/";
    public static String DBName = "";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "";
    public static Connection getConnection (){
        Connection c = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            c = DriverManager.getConnection(URL + DBName, USERNAME, PASSWORD);
            System.out.println("DB Connection success");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return c;
    }
}