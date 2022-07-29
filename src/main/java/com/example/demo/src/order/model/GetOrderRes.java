package com.example.demo.src.order.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
public class GetOrderRes {
    private String brandName;
    private String productName;
    private String sizeType;
    private Date createdAt;
    private Long buyId;
    private int totalPrice;
    private int productQuantity;
}
