package com.example.coffeeshopmanagement.model.entity;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor // Generates a no-args constructor
@AllArgsConstructor
public class Schedule {
    private int scheduleId;
    private Employee employee;
    private String dayOfWeek;
    private Date startTime;
    private Date endTime;
}
