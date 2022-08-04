package com.example.demo.src.basket.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostBasketProductRes {
    private int basketId;
    private int userId;
    private int productId;
    private int sizeid;
    private int productQuantity;
}
