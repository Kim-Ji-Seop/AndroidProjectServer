package com.example.demo.src.board.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
public class GetCommentsRes {
    private int commentIdx;
    private int userIdx;
    private String nickname;
    private String content;
    private String correctCreatedAt;
}
