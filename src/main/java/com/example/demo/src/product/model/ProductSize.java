package com.example.demo.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProductSize {
    private int sizeId;
    private int divId;//상품구분(상의,하의 ...)
    private String sizeType;//s,m,l,xl,230,240,250,260
    private int topHeight;// 상의 총장
    private int shoulder;// 상의 어깨
    private int chest;// 상의 가슴
    private int sleeveLength;// 상의 소매길이
    private int pantsHeight;// 바지 총장
    private int waist;// 바지 허리
    private int hip;// 바지 엉덩이
    private int thigh;// 바지 허벅지
    private int hem;// 바지 밑단
    private int rise;// 바지 밑위
}
