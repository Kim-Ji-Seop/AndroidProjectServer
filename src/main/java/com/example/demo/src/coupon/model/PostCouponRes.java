package com.example.demo.src.coupon.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostCouponRes {
    //BRAND_ID,COUPON_NAME,DISCOUNT_RATE,DISCOUNT_INFO
    private int brandId;
    private String couponName;
    private int discountRate;
    private String discountInfo;
}
