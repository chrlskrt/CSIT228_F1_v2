package com.example.csit228_f1_v2;

import com.example.csit228_f1_v2.SqlSide.ManageDatabase;
import com.example.csit228_f1_v2.SqlSide.Status;
import com.example.csit228_f1_v2.Utils.CurrentUser;
import com.example.csit228_f1_v2.Utils.Note;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class NotesHomeController {
    public Button btnLogOut;
    public CurrentUser currentUser = CurrentUser.getInstance();
    public VBox home_vbox;
    Alert alert = new Alert(Alert.AlertType.NONE);
    ManageDatabase dbManager = ManageDatabase.getInstance();

    void initialize(){
        try {
            loadNotes();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadNotes() throws IOException {
        ArrayList<Note> notes;

        notes = dbManager.readNotes();

        if (notes == null){
            return;
        }

        home_vbox.getChildren().clear();

        for(Note note : notes){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("note_pad.fxml"));
            AnchorPane note_component = loader.load();

            try {
                TextField title = (TextField) note_component.lookup("#noteTitle");
                TextField content = (TextField) note_component.lookup("#noteContent");
                Button delete_btn = (Button) note_component.lookup("#btnDeleteNote");
                Button edit_btn = (Button) note_component.lookup("#btnEditNote");
                Button cancelEdit_btn = (Button) note_component.lookup("#btnCancelEdit");
                Button updateEdit_btn = (Button) note_component.lookup("#btnUpdateNote");

//                System.out.println("NN " + note.note_id + " " + note.note_title + " " + note.note_content);

                title.setText(note.note_title);
                content.setText(note.note_content);

                delete_btn.setOnAction(e-> {
                    try {
                        deleteNote(note.note_id);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });

                edit_btn.setOnAction(e-> {
                    title.setEditable(true);
                    content.setEditable(true);
                    cancelEdit_btn.setVisible(true);
                    updateEdit_btn.setVisible(true);
                });

                cancelEdit_btn.setOnAction(e -> {
                    title.setText(note.note_title);
                    title.setEditable(false);

                    content.setText(note.note_content);
                    content.setEditable(false);

                    updateEdit_btn.setVisible(false);
                    cancelEdit_btn.setVisible(false);
                });

                updateEdit_btn.setOnAction(e -> {
                    Status res = updateNote(note.note_id, title.getText(), content.getText());

                    if (res == Status.NOTE_UPDATE_SUCCESS){
                        title.setEditable(false);
                        content.setEditable(false);

                        cancelEdit_btn.setVisible(false);
                        updateEdit_btn.setVisible(false);
                    }
                });
            } catch (Exception e){

            }

            home_vbox.getChildren().add(note_component);
        }
    }

    private Status updateNote(int noteId, String noteTitle, String noteContent) {
        Status res = dbManager.updateNote(noteId, noteTitle, noteContent);

        if (res == Status.NOTE_UPDATE_SUCCESS){
            showAlert(Alert.AlertType.INFORMATION, "Note updated successfully!");
        } else {
            showAlert(Alert.AlertType.ERROR, "There is an error while updating note.");
        }

        return res;
    }

    private void deleteNote (int note_id) throws IOException {
        Status res = dbManager.deleteNote(note_id);

        if (res == Status.NOTE_DELETION_SUCCESS){
            showAlert(Alert.AlertType.INFORMATION, "Note successfully deleted.");
            loadNotes();
        } else {
            showAlert(Alert.AlertType.ERROR, "Unable to delete note.");
        }
    }

    @FXML
    public void onLogOutButtonClick(ActionEvent actionEvent) throws IOException {
        currentUser.setUser_id(-1);
        currentUser.setUsername("");
        currentUser.setPassword("");

        goToLogIn(actionEvent);
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

    public void onBtnNewNoteClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("new_note.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 700, 500);
        stage.setScene(scene);
        stage.show();
        stage.setTitle("Create Note Page");
    }

    public void onBtnNotePadClick(ActionEvent actionEvent) {
    }

    void showAlert(Alert.AlertType alertType, String content){
        alert.setAlertType(alertType);
        alert.setContentText(content);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    void goToLogIn(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("login-view.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 700, 500);
        stage.setScene(scene);
        stage.show();
        stage.setTitle("Log-in Page");
    }
}
