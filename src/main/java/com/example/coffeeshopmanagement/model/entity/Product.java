package com.example.coffeeshopmanagement.model.entity;


import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor // Generates a no-args constructor
@AllArgsConstructor
public class Product {
    private int productId;
    private String productName;
    private String description;
    private String category;
    private String image;
    private int quantity;
    private double price;
    private String ingredients;
    private boolean availability;

    public Product(int productId,String productName,double price, String image){
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.image = image;
    }
    public Product(int productId,String productName,int quantity,double price){
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
    }
}
