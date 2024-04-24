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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class NewNoteController {

    public TextField tfNoteTitle;
    public TextArea taNoteContent;
    ManageDatabase dbManager = ManageDatabase.getInstance();
    Alert alert = new Alert(Alert.AlertType.NONE);
    CurrentUser currentUser = CurrentUser.getInstance();

    public void onLogOutButtonClick(ActionEvent actionEvent) throws IOException {
        currentUser.setUser_id(-1);
        currentUser.setUsername("");
        currentUser.setPassword("");

        goToLogIn(actionEvent);
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

    public void onBtnProfileClick(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("profile.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        ProfileSettingsController controller = loader.getController();
        Scene scene = new Scene(root, 700, 500);
        stage.setScene(scene);
        controller.initialize();
        stage.show();
        stage.setTitle("Profile Page");
    }

    public void onBtnNewNoteClick(ActionEvent actionEvent) {
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

    public void onBtnCreateNote(ActionEvent actionEvent) {
        String note_title = tfNoteTitle.getText();
        String note_content = taNoteContent.getText();

        if (note_content.isEmpty()){
            showAlert(Alert.AlertType.WARNING, "Note Content is empty. Cannot create new note.");
            return;
        }

        if (note_title.isEmpty()){
            note_title = "untitled";
        }
        Status noteRes = dbManager.createNote(note_title, note_content);

        if (noteRes == Status.NOTE_CREATION_SUCCESS){
            showAlert(Alert.AlertType.INFORMATION, note_title + " is successfully created!");
            tfNoteTitle.setText("");
            taNoteContent.setText("");
        } else {
            showAlert(Alert.AlertType.ERROR, "Note not created.");
        }
    }

}
