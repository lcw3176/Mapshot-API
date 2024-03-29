package com.mapshot.api.presentation.notice.model;

import com.mapshot.api.domain.notice.NoticeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticeListResponse {

    private long id;
    private String noticeType;
    private String title;
    private LocalDateTime createdDate;

    public static NoticeListResponse fromEntity(NoticeEntity noticeEntity) {
        return NoticeListResponse.builder()
                .id(noticeEntity.getId())
                .noticeType(noticeEntity.getNoticeType().getKorean())
                .title(noticeEntity.getTitle())
                .createdDate(noticeEntity.getCreatedDate())
                .build();
    }
}
