package com.mapshot.api.domain.news.client.naver;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NaverNewsDto {
    private String title;
    private String originallink; // 오타 아님, 변경시 파싱 에러남
    private String link;
    private String description;
    private String pubDate;

    public LocalDateTime getPubDate() {
        return LocalDateTime.parse(pubDate, DateTimeFormatter.RFC_1123_DATE_TIME);
    }
}
