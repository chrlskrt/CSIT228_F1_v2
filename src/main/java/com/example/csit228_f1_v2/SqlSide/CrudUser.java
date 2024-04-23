package com.example.csit228_f1_v2.SqlSide;

import com.example.csit228_f1_v2.NotesHomeController;

import java.sql.*;

public class CrudUser {
    private static CrudUser instance = null;

    private CrudUser(){}

    public static CrudUser getInstance(){
        if (instance == null){
            instance = new CrudUser();
        };

        return instance;
    }

    public int createUser(String username, String password){
        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement pStmt = c.prepareStatement(
                     "INSERT into users (username, password) VALUES (?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS
             )){

            pStmt.setString(1, username);
            pStmt.setString(2, password.hashCode() + "");

            int res = pStmt.executeUpdate();
            if (res == 0){
                return 0; // creation failed
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }

        return 1; // creation success
    }
}