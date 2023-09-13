package com.example.coffeeshopmanagement.controller;

import com.example.coffeeshopmanagement.database.JDBCConnect;
import com.example.coffeeshopmanagement.model.entity.Customer;
import com.example.coffeeshopmanagement.model.entity.Order;
import com.example.coffeeshopmanagement.model.entity.OrderItem;
import com.example.coffeeshopmanagement.model.entity.Product;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.sql.*;
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
    private Order order = new Order();
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
        // Kiểm tra và lấy thông tin khách hàng
        ShowProductController showProductController = new ShowProductController();
        showProductController.customerID();
        qty = quantity.getValue();

        String checkAvailable = "SELECT availability FROM product WHERE product_id = ?";
        String insertData = "INSERT INTO orders(order_id, customer_id, order_date, total_amount, payment_method) VALUES (?, ?, ?, ?, ?)";
        String updateProductQuantity = "UPDATE product SET quantity = quantity - ? WHERE product_id = ?";
        String selectStock = "SELECT quantity_in_stock FROM inventory WHERE product_id = ?";

        Connection connection = jdbcConnect.getJDBCConnection();
        PreparedStatement insertStatement = null;
        PreparedStatement updateQuantityStatement = null;

        try {
            // Kiểm tra sản phẩm có sẵn không
            PreparedStatement availabilityStatement = connection.prepareStatement(checkAvailable);
            availabilityStatement.setInt(1, products.getProductId());
            ResultSet availabilityResult = availabilityStatement.executeQuery();

            if (availabilityResult.next()) {
                String check = availabilityResult.getString("availability");
                if ("true".equals(check) || qty == 0) {
                    // Sản phẩm không sẵn có hoặc số lượng là 0
                    showErrorMessage("This product is not available.");
                    return;
                }
            }

            // Kiểm tra số lượng trong kho
            PreparedStatement stockStatement = connection.prepareStatement(selectStock);
            stockStatement.setInt(1, products.getProductId());
            ResultSet stockResult = stockStatement.executeQuery();

            int checkStock = 0;
            if (stockResult.next()) {
                checkStock = stockResult.getInt("quantity_in_stock");


                if (checkStock < products.getQuantity()) {
                    // Sản phẩm đã hết hàng
                    showErrorMessage("This product is out of stock.");
                    return;
                }
            }
            Customer customer = new Customer();
            Product product = new Product();
            product.setPrice(products.getPrice()); // Thay thế 10.0 bằng giá trị thực tế của sản phẩm

            // Gán đối tượng Product cho OrderItem
            order.setCustomer(customer);
            // Tạo và thêm order item
//            if (orderItem.getProduct() != null) {
            insertStatement = connection.prepareStatement(insertData);
            insertStatement.setInt(1, order.getOrderId());
            insertStatement.setInt(2,showProductController.customerID()); // Sử dụng customer ID đã lấy ở trước đó
            insertStatement.setString(3, String.valueOf(order.getOrderDate()));
            insertStatement.setString(4, order.getPaymentMethod());
            double subtotal = orderItem.getItemPrice() * qty - orderItem.getDiscount();
            insertStatement.setDouble(5, subtotal);
            insertStatement.executeUpdate();

//            } else {
//                showErrorMessage("error");
                // Xử lý khi orderItem.getProduct() là null, ví dụ: đặt giá trị mặc định hoặc hiển thị thông báo lỗi.
//            }
            // Cập nhật số lượng sản phẩm trong kho
            updateQuantityStatement = connection.prepareStatement(updateProductQuantity);
            updateQuantityStatement.setInt(1, qty);
            updateQuantityStatement.setInt(2, products.getProductId());
            updateQuantityStatement.executeUpdate();

            // Hiển thị thông báo thành công
            showSuccessMessage("Product added successfully!");

        } catch (SQLException e) {
            e.printStackTrace(); // Xử lý bất kỳ lỗi cơ sở dữ liệu nào ở đây
        } finally {
            try {
                if (insertStatement != null) {
                    insertStatement.close();
                }
                if (updateQuantityStatement != null) {
                    updateQuantityStatement.close();
                }
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Message");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccessMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
