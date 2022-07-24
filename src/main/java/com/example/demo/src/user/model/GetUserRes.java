package com.example.demo.src.user.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
public class GetUserRes {
    private String id;
    private String pw;
    private String userName;
    private String email;
    private String phoneNumber;
    private Date birth;
    private String sex;
    private int loginKakao;
    private String introduce;
    private Date createdAt;
    public GetUserRes(String id, String pw, String userName, String email, String phoneNumber){
        this.id = id;
        this.pw = pw;
        this.userName = userName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
}
