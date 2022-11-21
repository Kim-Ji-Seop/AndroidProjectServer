package com.example.demo.src.board.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
public class PostExamSubjectReq {
    private String title;
    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endAt;
}
