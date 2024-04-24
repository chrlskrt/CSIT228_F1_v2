package com.example.csit228_f1_v2;

import com.example.csit228_f1_v2.SqlSide.ManageDatabase;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class NotesApplication extends Application {
    ManageDatabase dbManager = ManageDatabase.getInstance();
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(NotesApplication.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 500, Color.BLACK);
        stage.setScene(scene);
        scene.setFill(Color.CORNFLOWERBLUE);
        dbManager.initDB();
        stage.setTitle("NOTES for u :)");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}