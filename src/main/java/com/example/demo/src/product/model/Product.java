package com.example.demo.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class Product {
    private int productId;
    private int brandId;
    private int categoryId;
    private String productPrice;
    private String productName;
    private String productImage;
    private String productInfo;
    private Date createdAt;
    private Date updatedAt;
    private int status;
}
