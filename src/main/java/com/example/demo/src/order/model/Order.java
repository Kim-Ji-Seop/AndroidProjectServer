package com.example.demo.src.order.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;

@Getter
@Setter
@AllArgsConstructor
public class Order {
    private int basketId;
    private Long buyId;
    private int totalPrice;
    private String request;
    private Time createdAt;
    private Time updatedAt;
}
