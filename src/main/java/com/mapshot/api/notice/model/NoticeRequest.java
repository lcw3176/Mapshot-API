package com.mapshot.api.notice.model;

import com.mapshot.api.notice.consts.NoticeConst;
import com.mapshot.api.notice.entity.NoticeEntity;
import com.mapshot.api.notice.enums.NoticeType;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;


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
