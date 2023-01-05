package com.joebrooks.mapshot.notice.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDetailResponse {

    private long id;
    private String noticeType;
    private String title;
    private String content;
    private LocalDateTime createdDate;
}
