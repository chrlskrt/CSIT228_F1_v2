package com.example.csit228_f1_v2;

import com.example.csit228_f1_v2.SqlSide.ManageDatabase;
import com.example.csit228_f1_v2.SqlSide.Status;
import com.example.csit228_f1_v2.Utils.CurrentUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

import java.io.IOException;

public class ProfileSettingsController {
    public PasswordField pfChangePass;
    public Label lblProfileUsername;
    ManageDatabase dbManager = ManageDatabase.getInstance();
    CurrentUser currentUser = CurrentUser.getInstance();
    Alert alert = new Alert(Alert.AlertType.NONE);
    void initialize(){
        lblProfileUsername.setText(currentUser.getUsername());
    }
    public void onBtnNotePadClick(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("home-view.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 700, 500);
        stage.setScene(scene);
        NotesHomeController controller = fxmlLoader.getController();
        controller.initialize();
        stage.show();
        stage.setTitle("Notes for u :)");
    }

    public void onBtnNewNoteClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("new_note.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 700, 500);
        stage.setScene(scene);
        stage.show();
        stage.setTitle("Create Note Page");
    }

    public void onLogOutButtonClick(ActionEvent actionEvent) throws IOException {
        currentUser.setUser_id(-1);
        currentUser.setUsername("");
        currentUser.setPassword("");

        goToLogIn(actionEvent);
    }

    public void onBtnDelAccClick(ActionEvent actionEvent) {
        Status res = dbManager.deleteUser();

        if (res == Status.ACCOUNT_DELETION_SUCCESS){
            showAlert(Alert.AlertType.INFORMATION, "Account deleted successfully.");
            try {
                goToLogIn(actionEvent);
            } catch (IOException ex) {

            }
        }
    }

    public void onBtnUpdateClick(ActionEvent actionEvent) {
        Status res = dbManager.updateUser(pfChangePass.getText());
        if (res == Status.ACCOUNT_UPDATE_SUCCESS){
            showAlert(Alert.AlertType.INFORMATION, "Account password successfully updated.");
            pfChangePass.setText("");
        } else {
            showAlert(Alert.AlertType.ERROR, "Account password update FAILED");
            pfChangePass.setText("");
        }
    }

    void goToLogIn(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("login-view.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 700, 500);
        stage.setScene(scene);
        stage.show();
        stage.setTitle("Log-in Page");
    }

    void showAlert(Alert.AlertType alertType, String content){
        alert.setAlertType(alertType);
        alert.setContentText(content);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
