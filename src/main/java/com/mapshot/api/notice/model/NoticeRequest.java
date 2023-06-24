package com.mapshot.api.notice.model;

import com.mapshot.api.notice.consts.NoticeConst;
import com.mapshot.api.notice.entity.NoticeEntity;
import com.mapshot.api.notice.enums.NoticeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticeRequest {
    
    @NotBlank
    private String noticeType;

    @NotBlank
    private String title;

    @NotBlank
    @Length(max = NoticeConst.MAX_CONTENT_LENGTH)
    private String content;

    public NoticeEntity toEntity() {
        return NoticeEntity.builder()
                .noticeType(NoticeType.valueOf(noticeType))
                .content(content)
                .title(title)
                .build();
    }
}
