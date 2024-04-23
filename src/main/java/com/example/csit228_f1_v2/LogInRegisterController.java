package com.example.csit228_f1_v2;

import com.example.csit228_f1_v2.SqlSide.CrudUser;
import com.example.csit228_f1_v2.SqlSide.MySQLConnection;
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

    public void onLogOutButtonClick(ActionEvent actionEvent) {
    }

    public void onLLoginButtonClick(ActionEvent actionEvent) {
        String username = tfLUsername.getText();
        String pass = pfLPassword.getText().hashCode() + "";

        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement stmt = c.prepareStatement(
                     "SELECT * FROM users WHERE username=? AND password=?"
             )) {
            stmt.setString(1, username);
            stmt.setString(2, pass);
            ResultSet res = stmt.executeQuery();

            if (res.next()){
                CurrentUser currentUser = CurrentUser.getInstance();
                currentUser.setUser_id(res.getInt("user_id"));
                currentUser.setUsername(res.getString("username"));
                currentUser.setPassword(res.getString("password"));

                goToHomePage(actionEvent);
                lblLogInError.setVisible(false);
            } else {
                lblLogInError.setVisible(true);
            }
        } catch (SQLException | IOException e) {

        }

        tfLUsername.setText("");
        pfLPassword.setText("");
    }
    public void onLSignUpButtonClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("register-view.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 700, 500);
        stage.setScene(scene);
        stage.show();
        stage.setTitle("Registration Page");
    }
    public void onRLogInButtonClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("login-view.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 700, 500);
        stage.setScene(scene);
        stage.show();
        stage.setTitle("CIT228 Site");
    }
    public void onRSignUpButtonClick(ActionEvent actionEvent) {
        String user = tfRUsername.getText();
        String pass = pfRPassword.getText();
        String cPass = pfRConfirm.getText();

        Alert alert = new Alert(Alert.AlertType.NONE);
        if (cPass.equals(pass)){
            CrudUser user_crud = CrudUser.getInstance();
            int createUserRes = user_crud.createUser(user, pass);

            if (createUserRes == 1){
                alert.setAlertType(Alert.AlertType.INFORMATION);
                alert.setContentText("Account creation successful!");
                alert.setHeaderText(null);
                alert.showAndWait();
            } else {
                alert.setAlertType(Alert.AlertType.WARNING);
                alert.setContentText("Account creation failed.");
                alert.setHeaderText(null);
                alert.showAndWait();
            }
        } else {
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setContentText("Passwords do not match.");
            alert.setHeaderText(null);
            alert.showAndWait();
        }

        pfRPassword.setText("");
        pfRConfirm.setText("");
    }

    void goToHomePage(ActionEvent event) throws IOException, SQLException {
        Parent root = FXMLLoader.load(getClass().getResource("home-view.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 700, 500);
        stage.setScene(scene);
        stage.show();
        stage.setTitle("Notes Site");
    }
}
