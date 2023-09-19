package com.example.coffeeshopmanagement.controller;

import com.example.coffeeshopmanagement.database.JDBCConnect;
import com.example.coffeeshopmanagement.model.entity.Customer;
import com.example.coffeeshopmanagement.model.entity.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class ShowProductController implements Initializable {
    @FXML
    private Button closeBtn;
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
    private TextField quantityInput;

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
    private ObservableList<Customer> customerList = FXCollections.observableArrayList();

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

    public void menuDisplayCard() {
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

// Lấy Tooltip từ productNameLabel trong controller khác
                Tooltip tooltip = productItemController.productNameLabel.getTooltip();

                // Đặt nội dung Tooltip thành tên sản phẩm
                tooltip.setText(cartList.get(i).getProductName());

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
    public void displayCustomerOrder(){
        for(int i = 0;i<customerList.size();i++){
            productItemController.setCusId(customerList.get(i));
        }
    }

    public ObservableList<Product> menuGetOrder() {
        customerID();
        ObservableList<Product> listData = FXCollections.observableArrayList();
        String sql = "SELECT c.customer_id, c.prod_name, c.quantity, p.price " +
                "FROM customer c " +
                "INNER JOIN product p ON c.product_id = p.product_id " +
                "WHERE c.customer_id = " + cID;

        Connection connection = jdbcConnect.getJDBCConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            Product product;
            while (resultSet.next()) {
                product = new Product(resultSet.getInt("customer_id"),
                        resultSet.getString("prod_name"),
                        resultSet.getInt("quantity"),
                        resultSet.getDouble("price"));
                listData.addAll(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }




    private ObservableList<Product> menuOrderListData;

    public void menuShowOrderData() {
        menuOrderListData = menuGetOrder();

        productNameColum.setCellValueFactory(new PropertyValueFactory<>("productName"));
        quantityColum.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceColum.setCellValueFactory(new PropertyValueFactory<>("price"));

//        menu_tableview.setItems(cartList);
        menu_tableview.setItems(FXCollections.observableArrayList(menuOrderListData));
    }

    private int getid;

    @FXML
    void menuSelectOrder(MouseEvent event) {
        Product product = menu_tableview.getSelectionModel().getSelectedItem();
        int num = menu_tableview.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1) {
            return;
        }
        getid = product.getProductId();

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

    public void menuDisplayTotal(){
        menuGetTotal();
        totalTextField.setText("$" + totalP);
    }

    private double amount;
    private double change;

    public void menuAmount(){
        menuGetTotal();
        if(quantityInput.getText().isEmpty() || totalP == 0){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Invalid :3");
            alert.showAndWait();
        }else {
            amount = Double.parseDouble(quantityInput.getText());

        }

    }
    public void menuPayBtn(){

        if(totalP ==0){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please choose your order first!");
            alert.showAndWait();
        }else{
            menuGetTotal();
            String insertPay = "INSERT INTO receipt(customer_id, total_amount, receipt_date) VALUES (?, ?, ?)";

            Connection connection = jdbcConnect.getJDBCConnection();
            try{

//                if(amount == 0){
//                    Alert alert = new Alert(Alert.AlertType.ERROR);
//                    alert.setTitle("Error Messaged");
//                    alert.setHeaderText(null);
//                    alert.setContentText("Something wrong :3");
//                    alert.showAndWait();
//                }else{
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Are you sure?");
                    Optional<ButtonType> option = alert.showAndWait();
                    if(option.get().equals(ButtonType.OK)){
                        customerID();
                        menuGetTotal();
                        PreparedStatement preparedStatement = connection.prepareStatement(insertPay);
                        preparedStatement.setInt(1, cID);
                        preparedStatement.setDouble(2, totalP);

                        Date date = new Date();
                        java.sql.Date sqlDate = new java.sql.Date(date.getTime());

                        preparedStatement.setString(3, String.valueOf(sqlDate));
                        preparedStatement.executeUpdate();

                        alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Infomation Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Successful.");
                        alert.showAndWait();

                        menuShowOrderData();
                        menuRestart();
                    }
                    else {
                        alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Infomation Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Cancelled.");
                        alert.showAndWait();
                    }
//                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void menuRestart(){
        totalP = 0;
        amount = 0;
        totalTextField.setText("$0.0");
        quantityInput.setText("0");
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
            if (resultSet1.next()) {
                checkID = resultSet1.getInt("MAX(customer_id)");
            }

            if (cID == 0) {
                cID += 1;
            } else if (cID == checkID) {
                cID += 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cID;
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
        menuDisplayCard();
        menuGetOrder();
        menuShowOrderData();
        menuDisplayTotal();

        displayCustomerOrder();

    }
}
