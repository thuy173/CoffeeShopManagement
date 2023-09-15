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
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
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
    private Spinner<Integer> prod_spinner;
    @FXML
    private TextField productIDtext;

    private Product products;
    private Order order = new Order();
    private OrderItem orderItem = new OrderItem();
    private Customer customer = new Customer();

    private Image image;

    private Integer productID;
    private LocalDateTime productDate;
    private String type;
    private String prod_image;
    private String description;

    private Integer product_quantity;

    private SpinnerValueFactory<Integer> spin;

    private Alert alert;
    private final JDBCConnect jdbcConnect;
    Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;


    public ProductItemController() {
        this.jdbcConnect = new JDBCConnect();
    }

    public void setData(Product product) {
        this.products = product;

        product_quantity = product.getQuantity();
        prod_image = product.getImage();
        type = product.getCategory();
        productID = product.getProductId();
        description = product.getDescription();
        productDate = LocalDateTime.now();

        productNameLabel.setText(product.getProductName());
        priceLabel.setText("$" + String.valueOf(product.getPrice()));

        String path = "File:" + product.getImage();
        image = new Image(path, 165, 140, false, true);
        imageProduct.setImage(image);

        pr = product.getPrice();
    }

    private int qty = 0;

    public void setQuantity() {
        spin = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0);

        prod_spinner.setValueFactory(spin);
    }

    private double totalP;
    private double pr;


    public void addBtn() {

        ShowProductController showProductController = new ShowProductController();
        showProductController.customerID();
        Product product = new Product();


        qty = prod_spinner.getValue();
        data d = new data();
        d.cID = 0;
        int check = 0;
        String checkAvailable = "SELECT availability FROM product WHERE product_id = '" +
                productID + "'";
        connection = jdbcConnect.getJDBCConnection();

        try {
            int checkstck = 0;
//            String checkStock = "SELECT quantity FROM product WHERE product_id = '" +
//                    productID + "'";
            String checkStock = "SELECT quantity FROM product WHERE product_id = ?";

            preparedStatement = connection.prepareStatement(checkStock);
            preparedStatement.setInt(1,data.id);
            resultSet = preparedStatement.executeQuery();

            System.out.println("ID  "+ productID);
            if (resultSet.next()) {
                checkstck = resultSet.getInt("quantity");
                System.out.println("quantity   "+ checkstck);
            }

            if (checkstck == 0) {

                String updateStock = "UPDATE product SET product_name = '"
                        + productNameLabel.getText() + "', category = '"
                        + type + "', quantity = 0, price = " + pr
                        + ", availability = 'false', image = '"
                        + prod_image + "', description = '"
                        + description + "' WHERE product_id = '"
                        + productID + "'";
                preparedStatement = connection.prepareStatement(updateStock);
                preparedStatement.executeUpdate();

            }

            preparedStatement = connection.prepareStatement(checkAvailable);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                check = resultSet.getInt("availability");
            }
            System.out.println("status:  "+checkstck);

            if (check == 0 && qty == 0) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Something Wrong :3");
                alert.showAndWait();
            } else {

                if (checkstck < qty) {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Invalid. This product is Out of stock");
                    alert.showAndWait();
                } else {
                    prod_image = prod_image.replace("\\", "\\\\");

                    String insertData = "INSERT INTO customer "
                            + "(customer_id, product_id, prod_name, type, quantity, price, date, image, em_username) "
                            + "VALUES(?,?,?,?,?,?,?,?,?)";
                    preparedStatement = connection.prepareStatement(insertData);
                    preparedStatement.setInt(1, d.cID);
                    preparedStatement.setString(2, String.valueOf(productID));
                    preparedStatement.setString(3, productNameLabel.getText());
                    preparedStatement.setString(4, type);
                    preparedStatement.setString(5, String.valueOf(qty));

                    totalP = (qty * pr);
                    preparedStatement.setString(6, String.valueOf(totalP));

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    LocalDateTime currentDateTime = LocalDateTime.now(); // Get the current date and time
                    String formattedDateTime = currentDateTime.format(formatter); // Format it as a string
//                    Date date = new Date();
//                    java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                    preparedStatement.setString(7, formattedDateTime);

                    preparedStatement.setString(8, prod_image);
                    preparedStatement.setString(9, data.username);

                    preparedStatement.executeUpdate();

                    int upStock = checkstck - qty;


                    System.out.println("Date: " + productDate);
                    System.out.println("Image: " + prod_image);

                    String updateStock = "UPDATE product SET product_name = '"
                            + productNameLabel.getText() + "', category = '"
                            + type + "', quantity = " + upStock + ", price = " + pr
                            + ", availability = '"
                            + check + "', description = '"
                            + description + "', ingredients = '"
                            + products.getIngredients() + "' WHERE product_id = '"
                            + productID + "'";

                    preparedStatement = connection.prepareStatement(updateStock);
                    preparedStatement.executeUpdate();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Added!");
                    alert.showAndWait();

                    showProductController.menuGetTotal();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Error adding product.");
            alert.showAndWait();
        }
    }


//    public void addBtn() {
//        ShowProductController showProductController = new ShowProductController();
//        showProductController.customerID();
//        int qty = quantity.getValue();
//
//        String checkAvailable = "SELECT availability FROM product WHERE product_id = ?";
//        String insertData = "INSERT INTO customer(customer_id, first_name, last_name, email, phone, loyalty_points) VALUES (?, ?, ?, ?, ?, ?)";
//        String updateProductQuantity = "UPDATE product SET quantity = quantity - ? WHERE product_id = ?";
//        String selectStock = "SELECT quantity_in_stock FROM inventory WHERE product_id = ?";
//
//        Connection connection = jdbcConnect.getJDBCConnection();
//        PreparedStatement insertStatement = null;
//        PreparedStatement updateQuantityStatement = null;
//
//        try {
//            PreparedStatement availabilityStatement = connection.prepareStatement(checkAvailable);
//            availabilityStatement.setInt(1, products.getProductId());
//            ResultSet availabilityResult = availabilityStatement.executeQuery();
//
//            if (availabilityResult.next()) {
//                String check = availabilityResult.getString("availability");
//                if ("false".equals(check) || qty == 0) {
//                    showErrorMessage("This product is not available.");
//                    return;
//                }
//            }
//
//            PreparedStatement stockStatement = connection.prepareStatement(selectStock);
//            stockStatement.setInt(1, products.getProductId());
//            ResultSet stockResult = stockStatement.executeQuery();
//
//            int checkStock = 0;
//            if (stockResult.next()) {
//                checkStock = stockResult.getInt("quantity_in_stock");
//
//                if (checkStock < qty) {
//                    showErrorMessage("This product is out of stock.");
//                    return;
//                }
//            }
//
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//            LocalDateTime currentDateTime = LocalDateTime.now(); // Get the current date and time
//            String formattedDateTime = currentDateTime.format(formatter); // Format it as a string
//
//            // Điền thông tin đơn hàng vào bảng orders
//            insertStatement = connection.prepareStatement(insertData);
//            insertStatement.setInt(1, customer.getCustomerId());
////            insertStatement.setInt(2, showProductController.customerID());
//            insertStatement.setString(2, customer.getFirstName());
//            insertStatement.setString(3, customer.getLastName());
//            insertStatement.setString(4,customer.getPhone());
//            insertStatement.setString(5, customer.getAddress());
//            insertStatement.setInt(6,customer.getLoyaltyPoints());
////            double subtotal = orderItem.getItemPrice() * qty - orderItem.getDiscount();
////            insertStatement.setDouble(5, subtotal);
//            insertStatement.executeUpdate();
//
//            // Cập nhật số lượng sản phẩm trong kho
//            updateQuantityStatement = connection.prepareStatement(updateProductQuantity);
//            updateQuantityStatement.setInt(1, qty);
//            updateQuantityStatement.setInt(2, products.getProductId());
//            updateQuantityStatement.executeUpdate();
//
//            showSuccessMessage("Product added successfully!");
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (insertStatement != null) {
//                    insertStatement.close();
//                }
//                if (updateQuantityStatement != null) {
//                    updateQuantityStatement.close();
//                }
//                connection.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private void showErrorMessage(String message) {
//        Alert alert = new Alert(Alert.AlertType.ERROR);
//        alert.setTitle("Error Message");
//        alert.setHeaderText(null);
//        alert.setContentText(message);
//        alert.showAndWait();
//    }
//
//    private void showSuccessMessage(String message) {
//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setTitle("Success");
//        alert.setHeaderText(null);
//        alert.setContentText(message);
//        alert.showAndWait();
//    }


    //        public void addBtn(){
//        ShowProductController showProductController = new ShowProductController();
//        showProductController.customerID();
//
//        qty = quantity.getValue();
//
//        String check = " ";
//        String checkAvailable = " SELECT availability FROM product WHERE product_id = '"
//                + productID +"'";
//
//        String insertData = "INSERT INTO customer(customer_id, first_name, last_name, email, phone, loyalty_points) VALUES (?, ?, ?, ?, ?, ?)";
//        String updateProductQuantity = "UPDATE product SET quantity = quantity - ? WHERE product_id = ?";
//        Connection connection = jdbcConnect.getJDBCConnection();
//
//        try{
//
//            int checkStock = 0;
//            String stock = "SELECT quantity FROM product WHERE product_id ='"
//                    + productID + "'";
//            preparedStatement = connection.prepareStatement(stock);
//            resultSet = preparedStatement.executeQuery();
//
//            if (resultSet.next()) {
//                checkStock = resultSet.getInt("quantity");
//            }
//            preparedStatement = connection.prepareStatement(checkAvailable);
//            resultSet = preparedStatement.executeQuery();
//
//            if(resultSet.next()){
//                check = resultSet.getString("availability");
//            }
//            if (checkStock == 0) {
//                String updateStock = "UPDATE product SET product_name = ?, description = ?, category = ?, image = ?, price = ?, quantity = 0, ingredients = ?, availability = false WHERE product_id = ?";
//                preparedStatement = connection.prepareStatement(updateStock);
//                preparedStatement.setString(1, products.getProductName());
//                preparedStatement.setString(2, products.getDescription());
//                preparedStatement.setString(3, products.getCategory());
//                preparedStatement.setString(4, products.getImage());
//                preparedStatement.setDouble(5, products.getPrice());
//                preparedStatement.setString(6, products.getIngredients());
//                preparedStatement.setInt(7, productID);
//                preparedStatement.executeUpdate();
//            }
//            if(check.equals("true") || qty == 0){
//                alert = new Alert(Alert.AlertType.ERROR);
//                alert.setTitle("Error Message");
//                alert.setHeaderText(null);
//                alert.setContentText("Something Wrong:3");
//            }else {
//
//
//                if (checkStock > qty) {
//                    alert = new Alert(Alert.AlertType.ERROR);
//                    alert.setTitle("Error Message");
//                    alert.setHeaderText(null);
//                    alert.setContentText("This is product Out of stock");
//                    alert.showAndWait();
//                }
//                else {
//
//
//                    preparedStatement = connection.prepareStatement(insertData);
//                    preparedStatement.setInt(1, customer.getCustomerId());
////            insertStatement.setInt(2, showProductController.customerID());
//                    preparedStatement.setString(2, customer.getFirstName());
//                    preparedStatement.setString(3, customer.getLastName());
//                    preparedStatement.setString(4,customer.getPhone());
//                    preparedStatement.setString(5, customer.getAddress());
//                    preparedStatement.setInt(6,customer.getLoyaltyPoints());
////            double subtotal = orderItem.getItemPrice() * qty - orderItem.getDiscount();
////            insertStatement.setDouble(5, subtotal);
//                    preparedStatement.executeUpdate();
//                    preparedStatement.executeUpdate();
//                    preparedStatement.close();
//
//                    // Cập nhật số lượng sản phẩm còn lại trong bảng product
//                     int unStock = qty - checkStock;
//                    preparedStatement = connection.prepareStatement(updateProductQuantity);
//                    preparedStatement.setInt(1, unStock);
//                    preparedStatement.setInt(2, productID);
//                    preparedStatement.executeUpdate();
//                    preparedStatement.close();
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
