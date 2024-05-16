package com.mapshot.api.domain.news;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NaverNewsResponse {
    private LocalDateTime lastBuildDate;
    private Integer total;
    private Integer start;
    private Integer display;
    private List<NaverNewsDto> items;
}
