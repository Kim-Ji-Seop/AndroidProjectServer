package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
public class User {
    private int userIdx;
    private String id;
    private String password;
    private String nickName; // 닉네임
    private String department; // 전공
    private int grade; // 학년
}
