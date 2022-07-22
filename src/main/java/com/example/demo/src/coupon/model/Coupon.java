package com.example.demo.src.coupon.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class Coupon {
    private int couponId;
    private String couponName;
    private int discountRate;
    private String discountInfo;
    private Timestamp createdAt;
    private Timestamp couponEnd;
    private Timestamp updatedAt;
    private int status;
}
