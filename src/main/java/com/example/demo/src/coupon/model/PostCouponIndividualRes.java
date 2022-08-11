package com.example.demo.src.coupon.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostCouponIndividualRes {
    private String couponName;
    private String discountInfo;
    private String discountRate;
    private String brandImage;
}
