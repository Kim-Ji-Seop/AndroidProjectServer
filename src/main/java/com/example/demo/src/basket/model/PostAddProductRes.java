package com.example.demo.src.basket.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.security.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class PostAddProductRes {
    private int basketId;
    private int userId;
    private int productId;
    private int sizeid;
    private int productQuantity;
}
