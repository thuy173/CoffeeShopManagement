package com.example.coffeeshopmanagement.model.entity;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor // Generates a no-args constructor
@AllArgsConstructor
public class Employee {
    private int employeeId;
    private String firstName;
    private String lastName;
    private String jobTitle;
    private Date hireDate;
    private double salary;
}
