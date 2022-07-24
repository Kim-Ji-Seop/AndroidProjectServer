package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
public class User {
    private int userId;
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
    private Date updatedAt;
    private int status;

    //login 생성자
    public User(int userId, String id, String pw, String userName, String email){
        this.userId = userId;
        this.id = id;
        this.pw = pw;
        this.userName = userName;
        this.email = email;
    }
}
