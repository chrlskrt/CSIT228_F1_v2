package com.example.csit228_f1_v2;

import com.example.csit228_f1_v2.Utils.CurrentUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
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