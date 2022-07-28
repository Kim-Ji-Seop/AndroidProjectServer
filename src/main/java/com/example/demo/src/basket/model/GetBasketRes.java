package com.example.demo.src.basket.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetBasketRes {
    private int userId;
    private String userName;
    private String productImage;
    private String productInfo;
    private String productName;
    private String sizeType;
    private int stock;
    private int productPrice;
    private int productQuantity;
    private int totalPrice;
}
