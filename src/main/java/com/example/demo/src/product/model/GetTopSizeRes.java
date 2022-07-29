package com.example.demo.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetTopSizeRes extends GetSizeRes{
    private int topHeight;// 상의 총장
    private int shoulder;// 상의 어깨
    private int chest;// 상의 가슴
    private int sleeveLength;// 상의 소매길이

    public GetTopSizeRes(String sizeType,int topHeight,int shoulder, int chest, int sleeveLength) {
        super(sizeType);
        this.topHeight=topHeight;
        this.shoulder=shoulder;
        this.chest=chest;
        this.sleeveLength=sleeveLength;
    }
}
