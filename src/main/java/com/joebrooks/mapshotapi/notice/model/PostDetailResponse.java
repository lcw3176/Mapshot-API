package com.joebrooks.mapshotapi.notice.model;

import com.joebrooks.mapshotapi.repository.notice.NoticeType;
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
    private NoticeType noticeType;
    private String title;
    private String content;
    private LocalDateTime createdDate;
}
