package com.example.coffeeshopmanagement.controller;

import com.example.coffeeshopmanagement.database.JDBCConnect;
import javafx.collections.FXCollections;
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
    private ChoiceBox<AccountType> choice_role;
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

        choice_role.setItems(FXCollections.observableArrayList(AccountType.CUSTOMER,AccountType.ADMIN));
        choice_role.setValue(new HomeController().getLoginAccount());
        choice_role.valueProperty().addListener(observable -> new HomeController().setLoginAccount(choice_role.getValue()));
    }

    @FXML
    void handleLogin(ActionEvent event) {
        String name = username.getText();
        String pass = password.getText();
        AccountType roleValue = choice_role.getValue();
        if (isValidCredentials(name, pass,roleValue)) {
            data.usernameadmin = name;
            String userRole = getUserRoleFromDatabase(name) ;

            // Tạo một Task để chờ 2 giây
            Task<Void> waitTask = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    Thread.sleep(2000); // Đợi 3 giây
                    return null;
                }
            };

            // Gán hành động khi Task hoàn thành (sau 2 giây)
            waitTask.setOnSucceeded(eventTask -> {
//                successAlert.showAndWait();
//                stageManager.closeStage();
                stage.close();
//                stageManager.loadHomeStage(); // Load home page sau khi chờ 3 giây
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

                    HomeController homeController = fxmlLoader.getController(); // Lấy tham chiếu đến HomeController từ FXMLLoader
                    homeController.setLoginController(this);
                    homeController.setUserRole(userRole);
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


    // Hàm kiểm tra thông tin đăng nhập dựa trên vai trò
    private boolean isValidCredentials(String username, String password, AccountType role) {
        boolean isValid = false;

        // Truy vấn cơ sở dữ liệu để kiểm tra cặp username và password dựa trên vai trò
        try {
            Connection connection = jdbcConnect.getJDBCConnection();
            String query = "SELECT * FROM admin WHERE username = ? AND password = ?";

            if (role == AccountType.ADMIN) {
                // Nếu là admin, thực hiện kiểm tra cho admin
                query += " AND role_id = 1"; // 1 là ID của vai trò admin trong bảng role
            } else if (role == AccountType.CUSTOMER) {
                // Nếu là customer, thực hiện kiểm tra cho customer
                query += " AND role_id = 2"; // 2 là ID của vai trò customer trong bảng role
            }

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


//    private boolean isValidCredentials(String username, String password, AccountType role) {
//        boolean isValid = false;
//        // Truy vấn cơ sở dữ liệu để kiểm tra cặp username và password
//        try {
//            Connection connection = jdbcConnect.getJDBCConnection();
//            String query = "SELECT a.*, r.name AS user_role FROM admin a INNER JOIN role r ON a.role_id = r.role_id WHERE a.username = ? AND a.password = ?";
//            PreparedStatement preparedStatement = connection.prepareStatement(query);
//            preparedStatement.setString(1, username);
//            preparedStatement.setString(2, password);
//            ResultSet resultSet = preparedStatement.executeQuery();
//
//            if (resultSet.next()) {
//                String userRole = resultSet.getString("user_role");
//                // Kiểm tra vai trò của người dùng
//                if (role == AccountType.ADMIN && "admin".equals(userRole)) {
//                    isValid = true;
//                } else if (role == AccountType.CUSTOMER && "customer".equals(userRole)) {
//                    isValid = true;
//                }
//            }
//
//            // Đóng các tài nguyên
//            resultSet.close();
//            preparedStatement.close();
//            connection.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return isValid;
//    }


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
    private String getUserRoleFromDatabase(String username) {
        String userRole = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            // Kết nối đến cơ sở dữ liệu
            connection = jdbcConnect.getJDBCConnection();

            // Truy vấn cơ sở dữ liệu để lấy vai trò dựa trên tên người dùng
            String query = "SELECT role.name FROM admin INNER JOIN role ON admin.role_id = role.role_id WHERE admin.username = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();

            // Nếu có kết quả trả về, lấy vai trò từ kết quả
            if (resultSet.next()) {
                userRole = resultSet.getString("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                // Đảm bảo đóng tất cả các tài nguyên
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return userRole;
    }


}
