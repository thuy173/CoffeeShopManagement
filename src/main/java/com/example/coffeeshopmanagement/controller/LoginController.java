package com.example.coffeeshopmanagement.controller;

import com.example.coffeeshopmanagement.database.JDBCConnect;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private Button close;
    @FXML
    private Button button;

    @FXML
    private PasswordField password;

    @FXML
    private TextField username;

    @FXML
    private Label wrongLogIn;
    private JDBCConnect jdbcConnect;

    private Stage stage;

    public void setSceneCurrentStage(Scene scene) {
        stage.setScene(scene);
    }

    public void setCurrentStage(Stage currentStage) {
        this.stage = currentStage;
    }

    public void showStage() {
        stage.show();
    }

    private StageManager stageManager;

    public LoginController() {
        this.jdbcConnect = new JDBCConnect();
        this.stageManager = new StageManager();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private double x = 0, y = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    void handleLogin(ActionEvent event) {
        String name = username.getText();
        String pass = password.getText();
        if (isValidCredentials(name, pass)) {
            // Tạo một Task để chờ 4 giây
            Task<Void> waitTask = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    Thread.sleep(2000); // Đợi 5 giây
                    return null;
                }
            };

            // Gán hành động khi Task hoàn thành (sau 4 giây)
            waitTask.setOnSucceeded(eventTask -> {
//                successAlert.showAndWait();
//                stageManager.closeStage();
                stage.close();
//                stageManager.loadHomeStage(); // Load home page sau khi chờ 5 giây
//                HomeController homeController = new HomeController();
//                homeController.loadScene("homeController.fxml",homeController);
                try {
                    Stage stagemain = new Stage();
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/coffeeshopmanagement/view/homeController.fxml"));
                    Parent root = fxmlLoader.load();
                    Scene scene = new Scene(root);
                    stagemain.setScene(scene);
                    setCurrentStage(stagemain);
                    stage.initStyle(StageStyle.TRANSPARENT);
                    showStage();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            // Bắt đầu Task
            new Thread(waitTask).start();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Error");
            alert.setHeaderText(null);
            alert.setContentText("Invalid credentials. Please try again.");
            alert.showAndWait();
        }
    }

    private boolean isValidCredentials(String username, String password) {
        boolean isValid = false;

        // Truy vấn cơ sở dữ liệu để kiểm tra cặp username và password
        try {
            Connection connection = jdbcConnect.getJDBCConnection();
            String query = "SELECT * FROM admin WHERE username = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();

            isValid = resultSet.next(); // Kiểm tra xem có kết quả trả về từ cơ sở dữ liệu hay không

            // Đóng các tài nguyên
            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isValid;
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
            stage = (Stage) close.getScene().getWindow();
            stage.close();

        }
    }
}
