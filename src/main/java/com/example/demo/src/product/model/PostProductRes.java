package com.example.demo.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostProductRes {
    private int brandId;
    private int divId;
    private int categoryId;
    private int productPrice;
    private String productName;
    private String productImage;
    private String productInfo;
}
