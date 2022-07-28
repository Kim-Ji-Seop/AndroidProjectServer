package com.example.demo.src.basket.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class Basket {
    private int basketId;
    private int userId;
    private int productId;
    private int productQuantity;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private int status;
}
