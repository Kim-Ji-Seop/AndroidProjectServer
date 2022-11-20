package com.example.demo.src.board.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetExamSubjectListRes {
    private int id;
    private String title;
    private String content;
    private String endAt;
}
