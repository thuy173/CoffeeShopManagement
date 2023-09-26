package com.example.coffeeshopmanagement.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class WarehouseController implements Initializable {
    @FXML
    private TextField ID;

    @FXML
    private Button closeBtn;

    @FXML
    private VBox displayItem;

    public WarehouseController() {
    }

    @FXML
    void close(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
