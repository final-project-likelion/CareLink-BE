package com.carelink.backend.training.news.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class NewsListResponseDto {

    private List<RecommendedNewsDto> recommended;
    private List<GeneralNewsDto> others;
}
