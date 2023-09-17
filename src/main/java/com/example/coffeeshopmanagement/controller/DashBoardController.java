package com.example.coffeeshopmanagement.controller;

import com.example.coffeeshopmanagement.database.JDBCConnect;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.ResourceBundle;

public class DashBoardController implements Initializable {
    @FXML
    private Label username;
    @FXML
    private Button closeBtn;
    @FXML
    private AreaChart<?, ?> customer_day;

    @FXML
    private BarChart<?, ?> daily_revenue;

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

//    income for day
    public void dashboardIncomeChart() {
        daily_revenue.getData().clear();

        // Get the current date
        LocalDate currentDate = LocalDate.now();

        // Define a date format for the SQL query
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Calculate the first day of the current month
        LocalDate firstDayOfMonth = currentDate.withDayOfMonth(1);

        String sql = "SELECT date, SUM(quantity) " +
                "FROM customer " +
                "WHERE date >= ? " +
                "GROUP BY date " +
                "ORDER BY TIMESTAMP(date)";

        Connection connection = jdbcConnect.getJDBCConnection();
        XYChart.Series chart = new XYChart.Series();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, firstDayOfMonth.format(dateFormat));
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                chart.getData().add(new XYChart.Data<>(resultSet.getString(1),
                        resultSet.getFloat(2)));
            }

            daily_revenue.getData().add(chart);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//customer quantity for day
    public void dashboardCustomerChartForCurrentDay() {
        customer_day.getData().clear();

        LocalDate currentDate = LocalDate.now();

        // Define a date format for the SQL query
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Calculate the first day of the current month
        LocalDate firstDayOfMonth = currentDate.withDayOfMonth(1);

        Connection connection = jdbcConnect.getJDBCConnection();
        XYChart.Series chart = new XYChart.Series();

        // Get the current year, month, and day
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // Month is zero-based, so we add 1
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        try {
            String sql = "SELECT date, COUNT(DISTINCT customer_id) " +
                    "FROM customer " +
                    "WHERE date >= ? " +
                    "GROUP BY date " +
                    "ORDER BY TIMESTAMP(date)";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, firstDayOfMonth.format(dateFormat));
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                chart.getData().add(new XYChart.Data<>(resultSet.getString(1),
                        resultSet.getFloat(2)));
            }

            customer_day.getData().add(chart);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        displayUsername();
        dashboardIncomeChart();
        dashboardCustomerChartForCurrentDay();
    }
}
