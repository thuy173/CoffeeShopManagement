package com.example.coffeeshopmanagement.model.entity;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor // Generates a no-args constructor
@AllArgsConstructor
public class FeedBack {
    private int feedbackId;
    private Customer customer;
    private Date feedbackDate;
    private String feedbackText;

}
