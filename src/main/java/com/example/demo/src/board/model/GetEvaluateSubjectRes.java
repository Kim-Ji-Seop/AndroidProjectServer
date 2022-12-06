package com.example.demo.src.board.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetEvaluateSubjectRes {
    private int id;
    private String subjectName;
    private String professor;
    private String separation;
    private int grade;
    private String time;
    private String room;
    private int credit;
    private float scoreAverage;
}
