package com.example.demo.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetSizeRes {
    private String sizeType;
    public GetSizeRes(String sizeType){
        this.sizeType=sizeType;
    }
}
