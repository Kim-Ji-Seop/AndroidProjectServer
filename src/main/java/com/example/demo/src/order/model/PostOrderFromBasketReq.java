package com.example.demo.src.order.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostOrderFromBasketReq {
    private String request;
    public PostOrderFromBasketReq(){

    }
    public PostOrderFromBasketReq(String request){
        this.request=request;
    }
}
