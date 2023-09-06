package com.example.coffeeshopmanagement.model.entity;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor // Generates a no-args constructor
@AllArgsConstructor
public class Order {
    private int orderId;
    private Customer customer;
    private Date orderDate;
    private double totalAmount;
    private String paymentMethod;
}
