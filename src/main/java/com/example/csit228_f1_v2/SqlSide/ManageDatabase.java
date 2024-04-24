package com.example.csit228_f1_v2.SqlSide;

import com.example.csit228_f1_v2.Utils.CurrentUser;
import com.example.csit228_f1_v2.Utils.Note;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.ArrayList;

public class ManageDatabase {
    private static ManageDatabase instance;
    private final CurrentUser currentUser = CurrentUser.getInstance();
    public static ManageDatabase getInstance(){
        if (instance == null){
            instance = new ManageDatabase();
        }

        return instance;
    }

    private ManageDatabase(){}

    public void initDB(){
        Statement stmt;
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
    public Status login(String username, String password){
        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement stmt = c.prepareStatement(
                     "SELECT * FROM users WHERE username=? AND password=?"
             )) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet res = stmt.executeQuery();

            if (res.next()){
                currentUser.setUser_id(res.getInt("user_id"));
                currentUser.setUsername(res.getString("username"));
                currentUser.setPassword(res.getString("password"));

                return Status.LOGIN_SUCCESS;
            }
        } catch (SQLException e) {
            return Status.LOGIN_FAILED;
        }

        return Status.LOGIN_FAILED;
    }
    public Status createUser(String username, String password){
        if (validateUserName(username) == 0){
            return Status.REGISTRATION_FAILED;
        };

        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement pStmt = c.prepareStatement(
                     "INSERT INTO users (username,password) VALUES (?,?)"
             )){
            String hashPass = password.hashCode() + "";
            pStmt.setString(1, username);
            pStmt.setString(2, hashPass);
            int res = pStmt.executeUpdate();
            if (res == 1){
                return Status.REGISTRATION_SUCCESS; // creation failed
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return Status.REGISTRATION_FAILED;
        }

        return Status.REGISTRATION_FAILED; // creation success
    }

    private int validateUserName(String username) {
        try (Connection c = MySQLConnection.getConnection();
        PreparedStatement pStmt = c.prepareStatement(
                "SELECT user_id FROM users WHERE username=?"
        )){
            pStmt.setString(1, username);
            ResultSet set = pStmt.executeQuery();

            if (!set.next()){
                return 1;
            }
        } catch (SQLException e) {
            return -1;
        }

        return 0;
    }

    public Status updateUser(String new_value){
        try (Connection c = MySQLConnection.getConnection();
        PreparedStatement pStmt = c.prepareStatement(
               "UPDATE users SET password=? WHERE user_id=?"
        )) {
            pStmt.setString(1, new_value.hashCode() + "");
            pStmt.setInt(2, currentUser.getUser_id());

            int res = pStmt.executeUpdate();

            if (res == 1){
                return Status.ACCOUNT_UPDATE_SUCCESS;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return Status.ACCOUNT_UPDATE_FAILED;
        }

        return Status.ACCOUNT_UPDATE_FAILED;
    }

    public Status deleteUser(){
        try (Connection c = MySQLConnection.getConnection();
            PreparedStatement pStmt = c.prepareStatement(
                    "DELETE FROM users WHERE user_id=?"
            )){
            pStmt.setInt(1, currentUser.getUser_id());

            int res = pStmt.executeUpdate();

            if (res == 1){
                currentUser.setUser_id(-1);
                currentUser.setUsername("");
                currentUser.setPassword("");

                return Status.ACCOUNT_DELETION_SUCCESS;
            }
        } catch (SQLException e) {
            return Status.ACCOUNT_DELETION_FAILED;
        }

        return Status.ACCOUNT_DELETION_FAILED;
    }

    public Status createNote(String note_title, String note_content){
        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement pStmt = c.prepareStatement(
                     "INSERT into notes (user_id, note_title, note_content) VALUES (?, ?, ?)"
             )) {

            pStmt.setInt(1, currentUser.getUser_id());
            pStmt.setString(2, note_title);
            pStmt.setString(3, note_content);

            int res = pStmt.executeUpdate();
            if (res == 1){
                return Status.NOTE_CREATION_SUCCESS;
            }
        } catch (SQLException e) {
            return Status.NOTE_CREATION_FAILED;
        }

        return Status.NOTE_CREATION_FAILED;
    }

    public ArrayList<Note> readNotes(){
        ArrayList<Note> notes = new ArrayList<>();

        try (Connection c = MySQLConnection.getConnection();
        PreparedStatement pStmt = c.prepareStatement(
                "SELECT * from notes WHERE user_id=?"
        )) {
            pStmt.setInt(1, currentUser.getUser_id());
            ResultSet res = pStmt.executeQuery();
            while(res.next()){
                notes.add(new Note(res.getInt("note_id"), res.getString("note_title"), res.getString("note_content")));
//                System.out.println(res.getInt("note_id") + " " + res.getString("note_title") + " " + res.getString("note_content"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        notes = reverseNotes(notes);

        return notes;
    }

    private ArrayList<Note> reverseNotes(ArrayList<Note> notes){
        ArrayList<Note> new_notes = new ArrayList<>();

        for (int i = notes.size()-1; i >= 0; i--){
            new_notes.add(notes.get(i));
        }

        return new_notes;
    }

    public Status updateNote(int note_id, String new_note_title, String new_note_content){
        try (Connection c = MySQLConnection.getConnection();
        PreparedStatement pStmt = c.prepareStatement(
                "UPDATE notes SET note_title = ?, note_content = ? WHERE note_id = ?"
        )){
            pStmt.setString(1, new_note_title);
            pStmt.setString(2, new_note_content);
            pStmt.setInt(3, note_id);

            int res = pStmt.executeUpdate();

            if (res == 1){
                return Status.NOTE_UPDATE_SUCCESS;
            }
        } catch (SQLException e) {
            return Status.NOTE_UPDATE_FAILED;
        }

        return Status.NOTE_UPDATE_FAILED;
    }

    public Status deleteNote(int note_id){
        try (Connection c = MySQLConnection.getConnection();
        PreparedStatement pStmt = c.prepareStatement(
                "DELETE FROM notes WHERE note_id=?"
        )) {
            pStmt.setInt(1, note_id);

            int res = pStmt.executeUpdate();

            if (res == 1){
                return Status.NOTE_DELETION_SUCCESS;
            }
        } catch (SQLException e) {
            return Status.NOTE_DELETION_FAILED;
        }

        return Status.NOTE_DELETION_FAILED;
    }
}
