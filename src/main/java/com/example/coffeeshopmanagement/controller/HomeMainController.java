package com.example.coffeeshopmanagement.controller;

import com.example.coffeeshopmanagement.database.JDBCConnect;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class HomeMainController {
    private JDBCConnect jdbcConnect;

    private Stage stage;
    private StageManager stageManager = new StageManager();
    @FXML
    private Button closeBtn;

    public HomeMainController() {
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
}
