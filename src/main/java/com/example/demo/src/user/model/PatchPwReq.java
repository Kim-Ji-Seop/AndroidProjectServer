package com.example.demo.src.user.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatchPwReq {
    private String pw;
    public PatchPwReq(){

    }
    public PatchPwReq(String pw){
        this.pw = pw;
    }
}
