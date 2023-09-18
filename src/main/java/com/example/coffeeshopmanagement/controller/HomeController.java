package com.example.coffeeshopmanagement.controller;

import com.example.coffeeshopmanagement.database.JDBCConnect;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    @FXML
    private Button closeBtn;
    @FXML
    private Button dashboard_controller;

    @FXML
    private Button employee_controller;

    @FXML
    private Button home_controller;

    @FXML
    private Button showProduct_controller;

    @FXML
    private Button ware_controller;
    @FXML
    private Button editProduct;
    @FXML
    private BorderPane fullHomePage;

    private Button activeButton;
    private JDBCConnect jdbcConnect;

    private Stage stage;
    private StageManager stageManager = new StageManager();

    public HomeController() {
        this.jdbcConnect = new JDBCConnect();
        this.stageManager = new StageManager();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        HomeMainController homeMainController = new HomeMainController();
        loadScene("homemainController.fxml", homeMainController);
        activeButton = home_controller;
        highlightActiveButton();
    }

    public void loadScene(String fxmlFileName, Object controller) {
        Parent root = null;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/coffeeshopmanagement/view/" + fxmlFileName));
            fxmlLoader.setController(controller);
            root = fxmlLoader.load();

        } catch (Exception e) {
            e.printStackTrace();
        }
        fullHomePage.setCenter(root);
    }

    @FXML
    void toHome(MouseEvent event) {
//        stageManager.loadHomeStage();
//        stage.close();
        HomeMainController homeMainController = new HomeMainController();
        loadScene("homemainController.fxml", homeMainController);
        activeButton = home_controller; // Update the active button
        highlightActiveButton();
    }

    @FXML
    void toOder(MouseEvent event) {
//        stageManager.loadShowProduct();
//        stage.close();
        ShowProductController showProductController = new ShowProductController();
        loadScene("showProduct.fxml", showProductController);
        activeButton = showProduct_controller; // Update the active button
        highlightActiveButton();
    }

    @FXML
    void toDashBoard(MouseEvent event) {
//        stageManager.loadDashBoard();
//        stage.close();
        DashBoardController dashBoardController = new DashBoardController();
        loadScene("dashboardController.fxml", dashBoardController);
        activeButton = dashboard_controller; // Update the active button
        highlightActiveButton();
    }

    @FXML
    void toEditProduct(MouseEvent event) {
//        stageManager.loadEditProduct();
//        stage.close();
        EditProductController editProductController = new EditProductController();
        loadScene("editProductController.fxml", editProductController);
        activeButton = editProduct; // Update the active button
        highlightActiveButton();
    }

    @FXML
    void close(ActionEvent event) {

        stage.close();
    }
    private void highlightActiveButton() {
        // Reset the style of all menu buttons
        home_controller.setStyle("");
        showProduct_controller.setStyle("");
        dashboard_controller.setStyle("");
        employee_controller.setStyle("");
        ware_controller.setStyle("");
        editProduct.setStyle("");

        // Set the style of the active menu button
        activeButton.setStyle("-fx-border-width: 0 0 5 0 ; -fx-border-color: #876445");
    }
}
