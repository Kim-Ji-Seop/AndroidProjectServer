package com.example.demo.src.coupon.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class GetCouponRes {
    private String couponName;
    private String discountInfo;
    private String discountRate;
    private String brandImage;
}
