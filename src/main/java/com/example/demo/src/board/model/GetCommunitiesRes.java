package com.example.demo.src.board.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetCommunitiesRes {
    private int communutyIdx;
    private int userIdx;
    private int grade;
    private String title;
    private String content;
    private String createdAt;
    private int commentCount;
    private String correctCreatedAt;
    private String nickname;
}
