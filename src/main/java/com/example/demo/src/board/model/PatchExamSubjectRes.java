package com.example.demo.src.board.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
public class PatchExamSubjectRes {
    private int listIdx;
    private int userIdx;
    private String title;
    private String content;
    private Date endAt;
}
