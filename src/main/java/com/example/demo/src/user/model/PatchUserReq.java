package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor // 기본 전체 생성자 lombok
public class PatchUserReq {
    private int USER_ID;
    private String ID;
    private String PW;
    private String USER_NAME;
    private String EMAIL;
    private String PHONE_NUMBER;
    private String ACCOUNT;
    private String ADDRESS;
    private String INTRODUCE;
    private Timestamp UPDATED_AT;
    private int STATUS;

    // 비밀번호 변경 시 필요한 생성자.
    public PatchUserReq(int USER_ID, String PW) {
        this.USER_ID = USER_ID;
        this.PW = PW;
    }
    public PatchUserReq(int USER_ID, int STATUS) {
        this.USER_ID = USER_ID;
        this.STATUS = STATUS;
    }
}

