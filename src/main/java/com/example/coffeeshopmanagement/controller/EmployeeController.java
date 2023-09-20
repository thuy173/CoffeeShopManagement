package com.example.coffeeshopmanagement.controller;

import com.example.coffeeshopmanagement.database.JDBCConnect;
import com.example.coffeeshopmanagement.model.entity.Employee;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class EmployeeController implements Initializable {

    @FXML
    private TextField ID;

    @FXML
    private Button addButton;

    @FXML
    private Button closeBtn;

    @FXML
    private Button deleteButton;

    @FXML
    private TableColumn<Employee, Integer> employeeID;
    @FXML
    private TableColumn<Employee, Integer> firstname;
    @FXML
    private TableColumn<Employee, Integer> lastname;
    @FXML
    private TableColumn<Employee, Integer> job;
    @FXML
    private TableColumn<Employee, Integer> salary;

    @FXML
    private TextField firstnameLabel;

    @FXML
    private TableColumn<Employee, String> hireDate;

    @FXML
    private DatePicker hiredateText;

    @FXML
    private Button export;

    @FXML
    private TextArea jobText;

    @FXML
    private TextField lastnameLabel;

    @FXML
    private TextField salaryLabel;

    @FXML
    private TableView<Employee> tableEmployee;

    @FXML
    private Button updateButton;

    private JDBCConnect jdbcConnect;

    private Stage stage;
    private StageManager stageManager = new StageManager();

    public EmployeeController() {
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
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
