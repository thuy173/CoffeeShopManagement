package com.example.coffeeshopmanagement.controller;

import com.example.coffeeshopmanagement.database.JDBCConnect;
import com.example.coffeeshopmanagement.model.entity.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ShowProductController implements Initializable {

    @FXML
    private AnchorPane mainOrder;

    @FXML
    private GridPane menu_gridPane;

    @FXML
    private ScrollPane menu_scroll;

    @FXML
    private TableView<Product> menu_tableview;

    @FXML
    private Button payBtn;

    @FXML
    private TableColumn<Product, Double> priceColum;

    @FXML
    private TableColumn<Product, String> productNameColum;

    @FXML
    private TableColumn<Product, Integer> quantityColum;

    @FXML
    private ComboBox<Integer> quantityInput;

    @FXML
    private TextField totalTextField;
    private JDBCConnect jdbcConnect;

    private Stage stage;
    private StageManager stageManager = new StageManager();

    public ShowProductController() {
        this.jdbcConnect = new JDBCConnect();
        this.stageManager = new StageManager();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private ObservableList<Product> cartList = FXCollections.observableArrayList();

    public ObservableList<Product> menuGetData() {
        String sql = "SELECT * FROM product WHERE availability = true";

        ObservableList<Product> listData = FXCollections.observableArrayList();


        Connection connection = jdbcConnect.getJDBCConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            Product pro;
            while (resultSet.next()) {
                pro = new Product(resultSet.getInt("product_id"),
                        resultSet.getString("product_name"),
                        resultSet.getDouble("price"),
                        resultSet.getString("image"));
                listData.add(pro);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listData;
    }

    private ProductItemController productItemController;

    public void menuDisplay() {
        cartList.clear();
        cartList.addAll(menuGetData());

        int row = 0;
        int col = 0;
        menu_gridPane.getRowConstraints().clear();
        menu_gridPane.getColumnConstraints().clear();

        for (int i = 0; i < cartList.size(); i++) {

            try {
                FXMLLoader load = new FXMLLoader();
                load.setLocation(getClass().getResource("/com/example/coffeeshopmanagement/view/productItem.fxml"));
                AnchorPane pane = load.load();
                productItemController = new ProductItemController();
                productItemController = load.getController();
                productItemController.setData(cartList.get(i));

                if (col == 3) {
                    col = 0;
                    row += 1;

                }
                menu_gridPane.add(pane, col++, row);

                GridPane.setMargin(pane, new Insets(15, 0, 8, 55));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private int cID;

    public int customerID() {
        cID = 0;

        String sql = "SELECT MAX(customer_id) FROM customer";
        Connection connection = jdbcConnect.getJDBCConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                cID = resultSet.getInt("MAX(customer_id)");
            }

            String checkCID = "SELECT MAX(customer_id) FROM receipt";
            PreparedStatement preparedStatement1 = connection.prepareStatement(checkCID);
            ResultSet resultSet1 = preparedStatement1.executeQuery();

            int checkID = 0;
            if(resultSet1.next()){
                checkID = resultSet1.getInt("MAX(customer_id)");
            }

            if(cID == 0 ){
                cID +=1;
            }else if(cID == checkID){
                cID +=1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cID;
    }
    private double totalP;
    public void menuGetTotal() {
        customerID();
        String total = "SELECT SUM(price) FROM customer WHERE customer_id = " + cID;

        Connection connection = jdbcConnect.getJDBCConnection();

        try {

            PreparedStatement preparedStatement = connection.prepareStatement(total);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                totalP = resultSet.getDouble("SUM(price)");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        menuDisplay();
    }
}
