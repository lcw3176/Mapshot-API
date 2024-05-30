package com.mapshot.api.application.notice;

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
public class NoticeDto {

    private long id;
    private String noticeType;
    private String title;
    private LocalDateTime createdDate;

    public static NoticeDto fromEntity(NoticeEntity noticeEntity) {
        return NoticeDto.builder()
                .id(noticeEntity.getId())
                .noticeType(noticeEntity.getNoticeType().getKorean())
                .title(noticeEntity.getTitle())
                .createdDate(noticeEntity.getCreatedDate())
                .build();
    }
}
