package com.mapshot.api.domain.notice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticeDetailResponse {

    private long id;
    private String noticeType;
    private String title;
    private String content;
    private LocalDateTime createdDate;


    public static NoticeDetailResponse fromEntity(NoticeEntity noticeEntity) {
        return NoticeDetailResponse.builder()
                .id(noticeEntity.getId())
                .noticeType(noticeEntity.getNoticeType().getKorean())
                .title(noticeEntity.getTitle())
                .content(noticeEntity.getContent())
                .createdDate(noticeEntity.getCreatedDate())
                .build();
    }
}
