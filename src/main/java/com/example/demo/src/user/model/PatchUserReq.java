package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor // 기본 전체 생성자 lombok
public class PatchUserReq {
    private int userId;
    private String id;
    private String pw;
    private String userName;
    private String email;
    private String phoneNumber;
    private String introduce;
    private Date UPDATED_AT;
    private int status;

    // 비밀번호 변경 시 필요한 생성자.
    public PatchUserReq(int userId, String pw) {
        this.userId = userId;
        this.pw = pw;
    }
    public PatchUserReq(int userId, int status) {
        this.userId = userId;
        this.status = status;
    }
}

