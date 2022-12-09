package com.example.demo.src.board.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetTopReviewRes {
    private String subjectName;
    private String professor;
    private String content;
    private float score;
}
