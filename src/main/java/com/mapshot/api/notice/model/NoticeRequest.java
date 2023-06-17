package com.mapshot.api.notice.model;

import com.mapshot.api.notice.entity.NoticeEntity;
import com.mapshot.api.notice.enums.NoticeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticeRequest {

    private Long id;
    private String noticeType;
    private String title;
    private String content;

    public NoticeEntity toEntity() {
        return NoticeEntity.builder()
                .noticeType(NoticeType.valueOf(noticeType))
                .content(content)
                .title(title)
                .build();
    }
}
