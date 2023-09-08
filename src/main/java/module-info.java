module com.example.coffeemanagement {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires javafx.web;
    requires lombok;
    requires org.apache.commons.io;

    opens com.example.coffeeshopmanagement to javafx.fxml;
    exports com.example.coffeeshopmanagement;
    exports com.example.coffeeshopmanagement.controller;
    opens com.example.coffeeshopmanagement.controller to javafx.fxml;
    opens com.example.coffeeshopmanagement.model.entity to javafx.base;
}