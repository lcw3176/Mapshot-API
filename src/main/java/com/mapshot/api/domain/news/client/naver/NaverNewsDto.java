package com.mapshot.api.domain.news.client.naver;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NaverNewsDto {
    private String title;
    private String originalLink;
    private String link;
    private String description;
    private LocalDateTime pubDate;
}
