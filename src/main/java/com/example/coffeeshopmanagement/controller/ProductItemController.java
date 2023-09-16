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

        description = product.getDescription();
        productDate = LocalDateTime.now();

        productIDtext.setText(String.valueOf(product.getProductId()));
        productNameLabel.setText(product.getProductName());
        priceLabel.setText("$" + String.valueOf(product.getPrice()));

        String path = "File:" + product.getImage();
        image = new Image(path, 165, 140, false, true);
        imageProduct.setImage(image);

        pr = product.getPrice();
        productID = product.getProductId();

    }

    private int qty = 0;

    public void setQuantity() {
        spin = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0);

        prod_spinner.setValueFactory(spin);
    }

    private double totalP;
    private double pr;

    private int productID;
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
            String checkStock = "SELECT quantity FROM product WHERE product_id = ?";
            preparedStatement = connection.prepareStatement(checkStock);
            preparedStatement.setInt(1,productID);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                checkstck = resultSet.getInt("quantity");
//                System.out.println("quantity   "+ checkstck);
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
//            System.out.println("status:  "+checkstck);

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
                    preparedStatement.setInt(2, productID);
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
                    preparedStatement.setString(9, d.username);

                    preparedStatement.executeUpdate();

                    int upStock = checkstck - qty;


//                    System.out.println("Date: " + productDate);
//                    System.out.println("Image: " + prod_image);

                    String updateStock = "UPDATE product SET product_name = '"
                            + productNameLabel.getText() + "', quantity = '"
                            + upStock + "', price = '"
                            + pr + "', availability = '"
                            + check + "'WHERE product_id = '"
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



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setQuantity();

    }
}
