package com.mapshot.api.notice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticeSummaryResponse {

    private long id;
    private String noticeType;
    private String title;
    private LocalDateTime createdDate;

}
