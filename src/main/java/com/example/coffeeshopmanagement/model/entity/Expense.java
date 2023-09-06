package com.example.coffeeshopmanagement.model.entity;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor // Generates a no-args constructor
@AllArgsConstructor
public class Expense {
    private int expenseId;
    private String expenseType;
    private Date expenseDate;
    private double amount;
    private String description;
}
