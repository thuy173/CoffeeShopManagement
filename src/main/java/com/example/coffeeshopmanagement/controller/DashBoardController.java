package com.example.coffeeshopmanagement.controller;

import com.example.coffeeshopmanagement.database.JDBCConnect;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class DashBoardController implements Initializable {
    @FXML
    private Label username;
    @FXML
    private Button closeBtn;

    private JDBCConnect jdbcConnect;

    private Stage stage;
    private StageManager stageManager = new StageManager();

    public DashBoardController() {
        this.jdbcConnect = new JDBCConnect();
        this.stageManager = new StageManager();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    void close(ActionEvent event) {
        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle("Confirmation");
        confirmationDialog.setHeaderText("Are you sure you want to CLOSE?");
        confirmationDialog.setContentText("Press OK to close or Cancel.");

        // Show the dialog and wait for a result
        ButtonType result = confirmationDialog.showAndWait().orElse(ButtonType.CANCEL);

        // If the user clicks OK, proceed with the logout
        if (result == ButtonType.OK) {
            // Close the current stage
            stage = (Stage) closeBtn.getScene().getWindow();
            stage.close();

        }
    }
    public void displayUsername(){
        String user = data.usernameadmin;
        if (user != null) {
            user = user.substring(0, 1).toUpperCase() + user.substring(1);
            username.setText(user);
        } else {
            username.setText("Default Username");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        displayUsername();
    }
}
