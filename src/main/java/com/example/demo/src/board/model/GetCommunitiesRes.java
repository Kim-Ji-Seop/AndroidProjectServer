package com.example.demo.src.board.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetCommunitiesRes {
    private int communutyIdx;
    private int grade;
    private String title;
    private String content;
    private String createdAt;
}
