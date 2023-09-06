package com.example.coffeeshopmanagement.model.entity;

import lombok.*;

import java.util.Date;


@Getter
@Setter
@ToString
@NoArgsConstructor // Generates a no-args constructor
@AllArgsConstructor
public class Transaction {
    private int transactionId;
    private Date transactionDate;
    private String description;
    private double income;
    private double expenses;
    private double profit;
}
