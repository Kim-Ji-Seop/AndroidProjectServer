package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class User {
    private int USER_ID;
    private String ID;
    private String PW;
    private String USER_NAME;
    private String EMAIL;
    private String PHONE_NUMBER;
    private Date BIRTH;
    private String SEX;
    private String ACCOUNT;
    private String ADDRESS;
    private int LOGIN_KAKAO;
    private String INTRODUCE;
    private Timestamp CREATED_AT;
    private Timestamp UPDATED_AT;
    private int STATUS;

    //login 생성자
    public User(int USER_ID,String ID, String PW, String USER_NAME, String EMAIL){
        this.USER_ID=USER_ID;
        this.ID=ID;
        this.PW=PW;
        this.USER_NAME=USER_NAME;
        this.EMAIL=EMAIL;
    }
}
