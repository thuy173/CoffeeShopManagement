package com.example.coffeeshopmanagement.model.entity;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor // Generates a no-args constructor
@AllArgsConstructor
public class Customer {
    //    private int customerId;
//    private String firstName;
//    private String lastName;
//    private String email;
//    private String phone;
//    private String address;
//    private int loyaltyPoints;
    private int customerId;
    private int productId;
    private String productName;
    private String category;
    private int quantity;
    private double total;
    private Date date;
    private String image;
    private String em_userName;

    public Customer(Integer productId, Integer customerID, Double total,
                    Date date, String emUsername) {
        this.productId = productId;
        this.customerId = customerID;
        this.total = total;
        this.date = date;
        this.em_userName = emUsername;
    }
}
