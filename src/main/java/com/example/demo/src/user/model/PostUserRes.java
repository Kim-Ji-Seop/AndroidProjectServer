package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class PostUserRes {
    private String jwt;
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
}
