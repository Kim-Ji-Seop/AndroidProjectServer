package com.example.demo.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.LinkedHashMap;

@Getter
@Setter
@AllArgsConstructor
public class PostProductReq {
    private String brandName;
    private String divName;
    private String categoryName;
    private int productPrice;
    private String productName;
    private String productImage;
    private String productInfo;
    LinkedHashMap<Object, Integer> sizeTypeAndStocks;
}
