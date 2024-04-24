package com.example.csit228_f1_v2;

import com.example.csit228_f1_v2.Utils.CurrentUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class NotesHomeController {
    public Button btnLogOut;
    public Label lblUserName;
    public CurrentUser currentUser = CurrentUser.getInstance();
    public VBox home_vbox;

    void initialize(){
        lblUserName.setText(currentUser.getUsername());
    }

    @FXML
    public void onLogOutButtonClick(ActionEvent actionEvent) throws IOException {
        currentUser.setUser_id(-1);
        currentUser.setUsername("");
        currentUser.setPassword("");

        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("login-view.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 700, 500);
        stage.setScene(scene);
        stage.show();
        stage.setTitle("Log-in Page");
    }

    public void onBtnProfileClick(ActionEvent actionEvent) {
        home_vbox.getChildren().clear();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(""));
    }

    public void onBtnNewNoteClick(ActionEvent actionEvent) {
    }

    public void onBtnNotePadClick(ActionEvent actionEvent) {
    }
}