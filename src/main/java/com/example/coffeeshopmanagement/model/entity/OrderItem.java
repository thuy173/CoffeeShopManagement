package com.example.coffeeshopmanagement.model.entity;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor // Generates a no-args constructor
@AllArgsConstructor
public class OrderItem {
    private int orderItemId;
    private Order order;
    private Product product;
    private int quantity;
    private double itemPrice;
    private double subtotal;
    private double discount;
}
