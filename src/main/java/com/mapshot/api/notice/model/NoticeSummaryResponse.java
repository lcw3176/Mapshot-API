package com.mapshot.api.notice.model;

import com.mapshot.api.notice.entity.NoticeEntity;
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

    public static NoticeSummaryResponse fromEntity(NoticeEntity noticeEntity) {
        return NoticeSummaryResponse.builder()
                .id(noticeEntity.getId())
                .noticeType(noticeEntity.getNoticeType().getKorean())
                .title(noticeEntity.getTitle())
                .createdDate(noticeEntity.getCreatedDate())
                .build();
    }
}
