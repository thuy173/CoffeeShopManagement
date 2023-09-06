package com.example.coffeeshopmanagement.model.entity;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor // Generates a no-args constructor
@AllArgsConstructor
public class Inventory {
    private int inventoryId;
    private Product product;
    private int quantityInStock;
    private int reorderPoint;
}
