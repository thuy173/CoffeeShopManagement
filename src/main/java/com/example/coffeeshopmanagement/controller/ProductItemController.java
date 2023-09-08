package com.example.coffeeshopmanagement.controller;

import com.example.coffeeshopmanagement.database.JDBCConnect;
import com.example.coffeeshopmanagement.model.entity.OrderItem;
import com.example.coffeeshopmanagement.model.entity.Product;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ProductItemController implements Initializable {

    @FXML
    private Button addBtn;

    @FXML
    private AnchorPane cardProduct;

    @FXML
    private ImageView imageProduct;

    @FXML
    private Label priceLabel;

    @FXML
    private Label productNameLabel;

    @FXML
    private Spinner<Integer> quantity;

    private Product products;
    private OrderItem orderItem = new OrderItem();

    private Image image;

    private Integer productID;

    private SpinnerValueFactory<Integer> spin;

    private Alert alert;
    private final JDBCConnect jdbcConnect;

    public ProductItemController() {
        this.jdbcConnect = new JDBCConnect();
    }

    public void setData(Product product) {
        this.products = product;
        productID = products.getProductId();
        productNameLabel.setText(product.getProductName());
        priceLabel.setText("$" + String.valueOf(product.getPrice()));
        String path = "File:" + product.getImage();
        image = new Image(path, 165, 140, false, true);
        imageProduct.setImage(image);
    }

    private int qty;

    public void setQuantity() {
        spin = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0);

        quantity.setValueFactory(spin);
    }

    public void addBtn() {

        ShowProductController mFrom = new ShowProductController();
        mFrom.customerID();

        qty = quantity.getValue();

        String checkAvailable = "SELECT availability FROM product WHERE product_id = ?";
        String insertData = "INSERT INTO order_item(order_item_id, quantity, item_price, discount, subtotal) VALUES ( ?, ?, ?, ?, ?)";
        String updateProductQuantity = "UPDATE product SET quantity = quantity - ? WHERE product_id = ?";
        String selectStock = "SELECT quantity_in_stock FROM inventory WHERE product_id = ?";

        Connection connection = jdbcConnect.getJDBCConnection();

        try {
            // Check product availability
            PreparedStatement availabilityStatement = connection.prepareStatement(checkAvailable);
            availabilityStatement.setString(1, String.valueOf(products.getProductId()));
            ResultSet availabilityResult = availabilityStatement.executeQuery();

            if (availabilityResult.next()) {
                String check = availabilityResult.getString("availability");

                if ("true".equals(check) || qty == 0) {
                    // Product is not available or quantity is 0
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Something Wrong:3");
                    alert.showAndWait();
                    return;
                }
            }


            // Check product stock
            PreparedStatement stockStatement = connection.prepareStatement(selectStock);
            stockStatement.setString(1, String.valueOf(products.getProductId()));
            ResultSet stockResult = stockStatement.executeQuery();

            int checkStock = 0;
            if (stockResult.next()) {
                checkStock = stockResult.getInt("quantity_in_stock");
              
            }

            if (checkStock < qty) {
                // Product is out of stock
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("This product is out of stock");
                alert.showAndWait();
                return;
            }
            products = new Product();
            products.setPrice(products.getPrice());
            orderItem.setProduct(products);
            // Insert order item
            PreparedStatement insertStatement = connection.prepareStatement(insertData);
            insertStatement.setInt(1, orderItem.getOrderItemId());
//            insertStatement.setString(2, String.valueOf(products.getProductId())); // Assuming productID is a String
            insertStatement.setInt(2, qty);
            insertStatement.setDouble(3, orderItem.getProduct().getPrice());
            insertStatement.setDouble(4, orderItem.getDiscount());
            double subtotal = orderItem.getItemPrice() * qty - orderItem.getDiscount();
            insertStatement.setDouble(5, subtotal);
            insertStatement.executeUpdate();
            insertStatement.close();

            // Update product quantity
            PreparedStatement updateQuantityStatement = connection.prepareStatement(updateProductQuantity);
            updateQuantityStatement.setInt(1, qty);
            updateQuantityStatement.setString(2, String.valueOf(products.getProductId()));
            updateQuantityStatement.executeUpdate();
            updateQuantityStatement.close();

            // Close all prepared statements and the connection here

            // Optionally, you can notify the user that the item has been added to the cart or order successfully.
        } catch (SQLException e) {
            e.printStackTrace(); // Handle any database-related exceptions here
        }
    }

//    public void addBtn(){
//        qty = quantity.getValue();
//
//        String check = " ";
//        String checkAvailable = " SELECT availability FROM product WHERE product_id = '"
//                + productID +"'";
//
//        String insertData = "INSERT INTO order_item(order_id, product_id, quantity, item_price, discount, subtotal) VALUES (?, ?, ?, ?, ?, ?)";
//        String updateProductQuantity = "UPDATE product SET quantity = quantity - ? WHERE product_id = ?";
//        Connection connection = jdbcConnect.getJDBCConnection();
//
//        try{
//            PreparedStatement preparedStatement = connection.prepareStatement(checkAvailable);
//            ResultSet resultSet = preparedStatement.executeQuery();
//
//            if(resultSet.next()){
//                check = resultSet.getString("availability");
//            }
//            if(check.equals("true") || qty == 0){
//                alert = new Alert(Alert.AlertType.ERROR);
//                alert.setTitle("Error Message");
//                alert.setHeaderText(null);
//                alert.setContentText("Something Wrong:3");
//            }else {
//
//                int checkStock = 0;
//                String stock = "SELECT quantity_in_stock FROM inventory WHERE product_id ='"
//                        + productID + "'";
//                PreparedStatement insertstock = connection.prepareStatement(stock);
//                ResultSet resultSetStock = insertstock.executeQuery();
//
//                if (resultSetStock.next()) {
//                    checkStock = resultSetStock.getInt("quantity_in_stock");
//                }
//
//                if (checkStock < quantity.getValue()) {
//                    alert = new Alert(Alert.AlertType.ERROR);
//                    alert.setTitle("Error Message");
//                    alert.setHeaderText(null);
//                    alert.setContentText("This is product Out of stock");
//                    alert.showAndWait();
//                } else {
//
//
//                    PreparedStatement insertStatement = connection.prepareStatement(insertData);
//                    insertStatement.setInt(1, orderItem.getOrderItemId());
//                    insertStatement.setInt(2, orderItem.getProduct().getProductId());
//                    insertStatement.setInt(3, orderItem.getQuantity());
//                    insertStatement.setDouble(4, orderItem.getProduct().getPrice());
//                    insertStatement.setDouble(5, orderItem.getDiscount());
//                    int quantityy = quantity.getValue();
//                    double subtotal = orderItem.getItemPrice() * quantityy - orderItem.getDiscount();
//                    insertStatement.setDouble(6, subtotal);
//                    insertStatement.executeUpdate();
//                    insertStatement.close();
//
//                    // Cập nhật số lượng sản phẩm còn lại trong bảng product
//                     int unStock = checkStock - quantityy;
//                    PreparedStatement updateStatement = connection.prepareStatement(updateProductQuantity);
//                    updateStatement.setInt(1, unStock);
//                    updateStatement.setInt(2, products.getProductId());
//                    updateStatement.executeUpdate();
//                    updateStatement.close();
//
//
//
//                    // Đã thêm sản phẩm vào đơn hàng thành công
//                    alert = new Alert(Alert.AlertType.INFORMATION);
//                    alert.setTitle("Success");
//                    alert.setHeaderText(null);
//                    alert.setContentText("Success added!");
//                    alert.showAndWait();
//                }
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//            alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Error Message");
//            alert.setHeaderText(null);
//            alert.setContentText("Error add product");
//            alert.showAndWait();
//        }
//
//    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setQuantity();
    }
}
