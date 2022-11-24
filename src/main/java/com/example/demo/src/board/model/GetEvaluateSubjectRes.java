package com.example.demo.src.board.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetEvaluateSubjectRes {
    private int id;
    private int grade;
    private String subjectName;
    private String professor;
}
