package com.example.csit228_f1_v2.SqlSide;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateDatabase {
    public void initDB(){
        Statement stmt = null;
        try (Connection c = MySQLConnection.getConnection()){
            stmt = c.createStatement();

            String createDBQuery = "CREATE DATABASE IF NOT EXISTS dbRepuesto;";
            stmt.execute(createDBQuery);

            MySQLConnection.DBName = "dbRepuesto";
            c.setCatalog("dbrepuesto");
            c.setAutoCommit(false);

            stmt = c.createStatement();

            String createTblUsersQuery = "CREATE TABLE IF NOT EXISTS users (" +
                    "user_id INT AUTO_INCREMENT PRIMARY KEY," +
                    "username VARCHAR (50) NOT NULL," +
                    "password VARCHAR (50) NOT NULL)";
            stmt.execute(createTblUsersQuery);

            String createTblNotesQuery = "CREATE TABLE IF NOT EXISTS notes (" +
                    "note_id INT AUTO_INCREMENT PRIMARY KEY," +
                    "user_id INT NOT NULL," +
                    "note_title VARCHAR(100) NOT NULL," +
                    "note_content VARCHAR(500) NOT NULL," +
                    "FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE)";
            stmt.execute(createTblNotesQuery);

            c.commit();
            System.out.println("Database with TABLES created successfully.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
