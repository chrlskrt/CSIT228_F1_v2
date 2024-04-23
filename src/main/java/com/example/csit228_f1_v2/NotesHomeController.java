package com.example.csit228_f1_v2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class NotesHomeController implements Initializable {
    public Button btnLogOut;
    public Label lblUserName;
    public CurrentUser currentUser = CurrentUser.getInstance();

    @FXML
    public void onLogOutButtonClick(ActionEvent actionEvent) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lblUserName.setText(currentUser.getUsername());
    }
}