package com.carelink.backend.training.news.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SixWDto {
    private String who;
    private String whenAt;
    private String whereAt;
    private String what;
    private String why;
    private String how;
}
