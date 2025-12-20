package com.carelink.backend.training.news.dto;

public record UserSixWSubmitRequest(
        String who,
        String whenAt,
        String whereAt,
        String what,
        String why,
        String how
) {}
