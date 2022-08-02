package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
public class PostUserReq {
    private String id;
    private String pw;
    private String userName;
    private String email;
    private String phoneNumber;
    private Date birth;
    private String sex;
    private int loginKakao;
    private String introduce;
}
