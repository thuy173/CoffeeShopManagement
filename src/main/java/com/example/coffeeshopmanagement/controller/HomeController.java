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
    private BorderPane fullHomePage;


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
    }

    public void loadScene(String fxmlFileName, Object controller) {
        Parent root = null;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/coffeeshopmanagement/view/" + fxmlFileName));
            fxmlLoader.setController(controller);
            root = fxmlLoader.load();
//            fullHomePage.setCenter(root);

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
    }

    @FXML
    void toOder(MouseEvent event) {
//        stageManager.loadShowProduct();
//        stage.close();
        ShowProductController showProductController = new ShowProductController();
        loadScene("showProduct.fxml", showProductController);
    }

    @FXML
    void toDashBoard(MouseEvent event) {
//        stageManager.loadDashBoard();
//        stage.close();
        DashBoardController dashBoardController = new DashBoardController();
        loadScene("dashboardController.fxml", dashBoardController);
    }

    @FXML
    void toEditProduct(MouseEvent event) {
//        stageManager.loadEditProduct();
//        stage.close();
        EditProductController editProductController = new EditProductController();
        loadScene("editProductController.fxml", editProductController);
    }

    @FXML
    void close(ActionEvent event) {

        stage.close();
    }

    public void setBoderColor() {

    }
}
