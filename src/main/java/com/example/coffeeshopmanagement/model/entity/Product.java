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

}
