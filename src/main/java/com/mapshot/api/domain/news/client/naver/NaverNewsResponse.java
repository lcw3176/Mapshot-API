package com.mapshot.api.domain.news.client.naver;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NaverNewsResponse {

    private String lastBuildDate;
    private Integer total;
    private Integer start;
    private Integer display;
    private List<NaverNewsDto> items;


    public LocalDateTime getLastBuildDate() {
        return LocalDateTime.parse(lastBuildDate, DateTimeFormatter.RFC_1123_DATE_TIME);
    }
}
