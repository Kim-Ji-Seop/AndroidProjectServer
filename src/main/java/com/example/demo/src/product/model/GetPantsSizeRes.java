package com.example.demo.src.product.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetPantsSizeRes extends GetSizeRes{
    private int pantsHeight;// 바지 총장
    private int waist;// 바지 허리
    private int hip;// 바지 엉덩이
    private int thigh;// 바지 허벅지
    private int hem;// 바지 밑단
    private int rise;// 바지 밑위

    public GetPantsSizeRes(String sizeType,int pantsHeight,int waist,int hip,int thigh,int hem,int rise) {
        super(sizeType);
        this.pantsHeight=pantsHeight;
        this.waist=waist;
        this.hip=hip;
        this.thigh=thigh;
        this.hem=hem;
        this.rise=rise;
    }
}
