package com.example.coffeeshopmanagement.controller;

import com.example.coffeeshopmanagement.model.entity.Product;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
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
    private Spinner<?> quantity;

    private Product products;

    private Image image;
    public void setData(Product product) {
        this.products = product;
        productNameLabel.setText(product.getProductName());
        priceLabel.setText(String.valueOf(product.getPrice()));
        String path = "File:" + product.getImage();
        image = new Image(path,165,140,false,true);
        imageProduct.setImage(image);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
