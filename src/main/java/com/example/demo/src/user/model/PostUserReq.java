package com.example.demo.src.user.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // 해당 클래스의 파라미터가 없는 생성자를 생성, 접근제한자를 PROTECTED로 설정.
public class PostUserReq {
    private String id; // 아이디
    private String pw; // 패스워드
    private String nickName; // 닉네임
    private String department; // 전공
    private int grade; // 학년
}
