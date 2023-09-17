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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

public class HomeMainController implements Initializable {
    private JDBCConnect jdbcConnect;

    private Stage stage;
    private StageManager stageManager = new StageManager();
    @FXML
    private Button closeBtn;

    @FXML
    private Label today_income;

    @FXML
    private Label total_customer;

    @FXML
    private Label total_product;
    @FXML
    private Label income_month;

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

    public void displayTotalCustomer(){
        String sql = " SELECT COUNT(customer_id) FROM receipt";
        Connection connection = jdbcConnect.getJDBCConnection();

        try{
            int numCustomer = 0;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){

                numCustomer = resultSet.getInt("COUNT(customer_id)");
            }
            total_customer.setText(numCustomer + " customer");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void displayTotalIncome(){
        Date date = new Date();
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        String sql = "SELECT SUM(total_amount) FROM receipt WHERE receipt_date ='"
                +sqlDate+"'";
        Connection connection = jdbcConnect.getJDBCConnection();
        try{

            double total = 0;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                total = resultSet.getDouble("SUM(total_amount)");
            }
            today_income.setText("$"+ total);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void displayTotalIncomeForCurrentMonth() {
        Connection connection = jdbcConnect.getJDBCConnection();
        try {
            double income = 0;

            // Get the current year and month
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1; // Month is zero-based, so we add 1

            String sql = "SELECT SUM(total_amount) FROM receipt WHERE YEAR(receipt_date) = ? AND MONTH(receipt_date) = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, year);
            preparedStatement.setInt(2, month);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                income = resultSet.getDouble(1); // You can use the index 1 to get the first column in the result set.
            }
            income_month.setText("$" + income);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void displayTotalProduct(){
        String sql = "SELECT SUM(quantity) FROM customer";
        Connection connection = jdbcConnect.getJDBCConnection();
        try {
            int quantity = 0 ;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                quantity = resultSet.getInt("SUM(quantity)");
            }
            total_product.setText(quantity + " product");

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        displayTotalCustomer();
        displayTotalIncome();
        displayTotalProduct();
        displayTotalIncomeForCurrentMonth();
    }
}
