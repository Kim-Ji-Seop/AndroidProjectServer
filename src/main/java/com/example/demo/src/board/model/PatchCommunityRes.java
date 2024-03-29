package com.example.demo.src.board.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PatchCommunityRes {
    private int communityIdx;
    private int userIdx;
    private String title;
    private String content;
}
