package com.example.csit228_f1_v2;

import com.example.csit228_f1_v2.SqlSide.ManageDatabase;
import com.example.csit228_f1_v2.SqlSide.MySQLConnection;
import com.example.csit228_f1_v2.SqlSide.Status;
import com.example.csit228_f1_v2.Utils.CurrentUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class LogInRegisterController {
    public Button btnLLogIn;
    public Button btnLSignUp;
    public TextField tfLUsername;
    public Label lblLogInError;
    public PasswordField pfLPassword;
    public TextField tfRUsername;
    public PasswordField pfRPassword;
    public Button btnRLogIn;
    public Button btnRSignUp;
    public Label lblRegisterError;
    public PasswordField pfRConfirm;
    public Alert alert = new Alert(Alert.AlertType.NONE);

    public void onLLoginButtonClick(ActionEvent actionEvent) throws SQLException, IOException {
        String username = tfLUsername.getText();
        String pass = pfLPassword.getText().hashCode() + "";

        ManageDatabase dbManager = ManageDatabase.getInstance();

        Status res = dbManager.login(username, pass);

        if (res == Status.LOGIN_SUCCESS){
            goToHomePage(actionEvent);
        } else {
            showAlert(Alert.AlertType.WARNING, "Usernane or password is incorrect.");
        }

        tfLUsername.setText("");
        pfLPassword.setText("");
    }
    public void onLSignUpButtonClick(ActionEvent actionEvent) throws IOException {
        goToRegister(actionEvent);
    }

    public void onRLogInButtonClick(ActionEvent actionEvent) throws IOException {
        goToLogIn(actionEvent);
    }
    public void onRSignUpButtonClick(ActionEvent actionEvent) throws IOException {
        String user = tfRUsername.getText();
        String pass = pfRPassword.getText();
        String cPass = pfRConfirm.getText();

        Alert alert = new Alert(Alert.AlertType.NONE);
        if (cPass.equals(pass)){
            ManageDatabase dbManager = ManageDatabase.getInstance();
            Status createUserRes = dbManager.createUser(user, pass);

            if (createUserRes == Status.REGISTRATION_SUCCESS){
                goToLogIn(actionEvent);
            } else {
                showAlert(Alert.AlertType.WARNING, "Account creation failed.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Passwords do not match.");
        }

        pfRPassword.setText("");
        pfRConfirm.setText("");
    }
    void showAlert(Alert.AlertType alertType, String content){
        alert.setAlertType(alertType);
        alert.setContentText(content);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    void goToHomePage(ActionEvent event) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("home-view.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 700, 500);
        stage.setScene(scene);
        NotesHomeController controller = fxmlLoader.getController();
        controller.initialize();
        stage.show();
        stage.setTitle("Notes for u :)");
    }

    void goToLogIn(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("login-view.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 700, 500);
        stage.setScene(scene);
        stage.show();
        stage.setTitle("Log-in Page");
    }

    void goToRegister(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("register-view.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 700, 500);
        stage.setScene(scene);
        stage.show();
        stage.setTitle("Registration Page");
    }
}