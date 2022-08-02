package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetEssentialInfoRes {
    private String id;
    private String pw;
    private String userName;
    private String email;
    private String phoneNumber;
}
