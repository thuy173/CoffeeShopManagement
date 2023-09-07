package com.example.coffeeshopmanagement.controller;

import com.example.coffeeshopmanagement.database.JDBCConnect;
import com.example.coffeeshopmanagement.model.entity.Product;
import com.example.coffeeshopmanagement.model.repository.ProductRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class EditProductController implements Initializable {
    @FXML
    private Button close;
    @FXML
    private TableView<Product> tableProduct;

    @FXML
    private Button addButton;

    @FXML
    private ComboBox<String> availabilityInput;

    @FXML
    private TableColumn<Product, String> availabilityProduct;

    @FXML
    private ComboBox<String> categoryInput;

    @FXML
    private TableColumn<Product, String> categoryProduct;

    @FXML
    private Button deleteButton;

    @FXML
    private TextArea descriptionInput;

    @FXML
    private TableColumn<Product, String> descriptionProduct;

    @FXML
    private TextField idInput;

    @FXML
    private TableColumn<Product, Integer> idProduct;

    @FXML
    private ImageView imageProductInput;

    @FXML
    private Button importImageButton;

    @FXML
    private TextArea ingredientsInput;

    @FXML
    private TableColumn<Product, String> ingredientsProduct;

    @FXML
    private TextField nameInput;

    @FXML
    private TableColumn<Product, String> nameProduct;

    @FXML
    private TextField priceInput;

    @FXML
    private TableColumn<Product, Double> priceProduct;

    @FXML
    private ComboBox<Integer> quantityInput;

    @FXML
    private TableColumn<Product, Integer> quantityProduct;

    @FXML
    private Button updateButton;
    private JDBCConnect jdbcConnect;

    private Stage stage;
    private StageManager stageManager = new StageManager();

    public EditProductController() {
        this.jdbcConnect = new JDBCConnect();
        this.stageManager = new StageManager();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private final ProductRepository productRepository = new ProductRepository();

//    private InstructorsService instructorsService = new InstructorsServiceImpl();


    //xử lí comboBox
    private String categorys[] = {"Coffee", "Tea", "Juice", "Cake"};

    public void categoryList(ComboBox<String> category) {
        if (category == null) {
            category = new ComboBox<>();
        }

        List<String> categorylist = new ArrayList<>();

        // Thêm giá trị mặc định vào danh sách
//        categorylist.add("Coffee"); // Đây là giá trị mặc định

        for (String data : categorys) {
            categorylist.add(data);
        }

        ObservableList<String> listCategory = FXCollections.observableArrayList(categorylist);
        category.setItems(listCategory);

        // Đặt giá trị mặc định
        category.getSelectionModel().selectFirst(); // Chọn giá trị đầu tiên trong danh sách
    }
    private String availabilitys[] = {"True", "False"};

    public void setAvailability(ComboBox<String> availability) {
        if (availability == null) {
            availability = new ComboBox<>();
        }

        List<String> availabilitylist = new ArrayList<>();

        // Thêm giá trị mặc định vào danh sách
//        availabilitylist.add("True"); // Đây là giá trị mặc định

        for (String data : availabilitys) {
            availabilitylist.add(data);
        }

        ObservableList<String> listCategory = FXCollections.observableArrayList(availabilitylist);
        availability.setItems(listCategory);

        // Đặt giá trị mặc định
        availability.getSelectionModel().selectFirst(); // Chọn giá trị đầu tiên trong danh sách
    }
    private Integer quantityList[] = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26};

    public void setQuantity(ComboBox<Integer> quantity) {
        if (quantity == null) {
            quantity = new ComboBox<>();
        }

        List<Integer> quantitylist = new ArrayList<>();

        // Thêm giá trị mặc định vào danh sách
//        quantitylist.add(1); // Đây là giá trị mặc định

        for (Integer data : quantityList) {
            quantitylist.add(data);
        }

        ObservableList<Integer> listCategory = FXCollections.observableArrayList(quantitylist);
        quantity.setItems(listCategory);

        // Đặt giá trị mặc định
        quantity.getSelectionModel().selectFirst(); // Chọn giá trị đầu tiên trong danh sách
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tableProduct.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                showProduct(newValue);
            }
        });
        loadProductData();
        categoryList(categoryInput);
        setAvailability(availabilityInput);
        setQuantity(quantityInput);
    }

    //show lên textfield
    private void showProduct(Product product) {

        idInput.setText(String.valueOf(product.getProductId()));
        nameInput.setText(product.getProductName());
        descriptionInput.setText(product.getDescription());
        categoryInput.setValue(product.getCategory());
        quantityInput.setValue(product.getQuantity());
        priceInput.setText(String.valueOf(product.getPrice()));
        ingredientsInput.setText(product.getIngredients());
//        availabilityInput.setValue(product.get);

    }

    //show lên bảng
    private void loadProductData() {
        idProduct.setCellValueFactory(new PropertyValueFactory<>("productId"));
        nameProduct.setCellValueFactory(new PropertyValueFactory<>("productName"));
        descriptionProduct.setCellValueFactory(new PropertyValueFactory<>("description"));
        categoryProduct.setCellValueFactory(new PropertyValueFactory<>("category"));
        quantityProduct.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceProduct.setCellValueFactory(new PropertyValueFactory<>("price"));
        ingredientsProduct.setCellValueFactory(new PropertyValueFactory<>("ingredients"));
        availabilityProduct.setCellValueFactory(new PropertyValueFactory<>("availability"));


        // Thực hiện truy vấn để lấy dữ liệu từ cơ sở dữ liệu
        // Giả định có phương thức để lấy danh sách giảng viên từ cơ sở dữ liệu
        List<Product> productList = productRepository.getAllProducts();
        // Đổ dữ liệu vào TableView
        tableProduct.getItems().addAll(productList);


    }

    //clear text
    public void clear() {
        idInput.clear();
        nameInput.clear();
        descriptionInput.clear();
//        categoryInput.setValue(null);
//        quantityInput.setValue(null);
        priceInput.clear();
        ingredientsInput.clear();
    }

    @FXML
    private void addProduct() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation message");
        alert.setHeaderText("Are you sure you want to add this product?");
        alert.setContentText("");

        ButtonType confirm = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        alert.getButtonTypes().setAll(confirm, ButtonType.CANCEL);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == confirm) {

            String nameProduct = nameInput.getText();
            String des = descriptionInput.getText();
            String category = categoryInput.getValue();
            Integer quantity = quantityInput.getValue();
            Double price = Double.valueOf(priceInput.getText());
            String ingredients = ingredientsInput.getText();
            Boolean availability = Boolean.valueOf(availabilityInput.getValue());

            Product newProduct = new Product();
            newProduct.setProductName(nameProduct);
            newProduct.setDescription(des);
            newProduct.setCategory(category);
            newProduct.setQuantity(quantity);
            newProduct.setPrice(price);
            newProduct.setIngredients(ingredients);
            newProduct.setAvailability(availability);
            productRepository.addProduct(newProduct);
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information message!");
            alert.setHeaderText(null);
            alert.setContentText("Successfully add!!");
            alert.showAndWait();
            tableProduct.getItems().add(newProduct);
            tableProduct.refresh();

        }

        clear();
    }

    @FXML
    private void updateProduct() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation message");
        alert.setHeaderText("Are you sure you want to update this product?");
        alert.setContentText("");

        ButtonType confirm = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        alert.getButtonTypes().setAll(confirm, ButtonType.CANCEL);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == confirm) {
            Product products = tableProduct.getSelectionModel().getSelectedItem();
            if(products != null){
                String nameProduct = nameInput.getText();
                String des = descriptionInput.getText();
                String category = categoryInput.getValue();
                Integer quantity = quantityInput.getValue();
                Double price = Double.valueOf(priceInput.getText());
                String ingredients = ingredientsInput.getText();
                Boolean availability = Boolean.valueOf(availabilityInput.getValue());

                products.setProductName(nameProduct);
                products.setDescription(des);
                products.setCategory(category);
                products.setQuantity(quantity);
                products.setPrice(price);
                products.setIngredients(ingredients);
                products.setAvailability(availability);

                productRepository.updateProduct(products);
                tableProduct.refresh();

                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information message!");
                alert.setHeaderText(null);
                alert.setContentText("Successfully update!!");
                alert.showAndWait();

                clear();

            }

        }

    }

    @FXML
    private void deleteProduct() {
        Product products = tableProduct.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation message");
        alert.setHeaderText("Are you sure you want to delete this product?");
        alert.setContentText("");

        ButtonType confirm = new ButtonType("Delete", ButtonBar.ButtonData.OK_DONE);
        alert.getButtonTypes().setAll(confirm, ButtonType.CANCEL);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == confirm) {
            if (products != null) {
                productRepository.deleteProduct(products.getProductId());
                // Cập nhật danh sách members sau khi xóa và hiển thị lại view
                tableProduct.getItems().remove(products);
                tableProduct.refresh();
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information message!");
                alert.setHeaderText(null);
                alert.setContentText("Successfully delete!!");
                alert.showAndWait();
                clear();

            }
        }


//        Product product = tableProduct.getSelectionModel().getSelectedItem();
//        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//        alert.setTitle("Confirmation message");
//        alert.setHeaderText("Are you sure you want to delete this product?");
//        alert.setContentText("");
//
//        ButtonType confirm = new ButtonType("Delete", ButtonBar.ButtonData.OK_DONE);
//        alert.getButtonTypes().setAll(confirm, ButtonType.CANCEL);
//        Optional<ButtonType> result = alert.showAndWait();
//
//        if (result.isPresent() && result.get() == confirm && product != null) {
//            // Đặt availability của sản phẩm thành false trong cơ sở dữ liệu
//            product.setAvailability(false);
//            productRepository.updateProduct(product);
//            FilteredList<Product> filteredList = new FilteredList<>(tableProduct.getItems(), p -> p.isAvailability());
//            tableProduct.setItems(filteredList);
//            // Cập nhật lại TableView để thể hiện thay đổi
//            tableProduct.refresh();
//            clear();
//        }

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
