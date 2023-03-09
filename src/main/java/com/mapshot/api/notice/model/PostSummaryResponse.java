package com.mapshot.api.notice.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostSummaryResponse {

    private long id;
    private String noticeType;
    private String title;
    private LocalDateTime createdDate;

}
