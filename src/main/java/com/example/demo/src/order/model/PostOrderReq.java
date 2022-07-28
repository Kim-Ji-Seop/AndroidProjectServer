package com.example.demo.src.order.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostOrderReq {
    private String request;
    public PostOrderReq(){

    }
    public PostOrderReq(String request){
        this.request=request;
    }
}
