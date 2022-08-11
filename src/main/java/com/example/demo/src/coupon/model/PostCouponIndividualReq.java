package com.example.demo.src.coupon.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostCouponIndividualReq {
    private int couponId;

    public PostCouponIndividualReq(){

    }
    public PostCouponIndividualReq(int couponId){
        this.couponId=couponId;
    }
}
