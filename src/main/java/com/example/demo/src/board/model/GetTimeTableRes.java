package com.example.demo.src.board.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetTimeTableRes {
    private int timetableIdx;
    private String subjectName;
    private String room;
    private String time;
}
