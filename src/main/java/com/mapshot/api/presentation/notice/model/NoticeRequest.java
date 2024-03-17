package com.mapshot.api.presentation.notice.model;

import com.mapshot.api.domain.notice.NoticeConst;
import com.mapshot.api.domain.notice.NoticeEntity;
import com.mapshot.api.domain.notice.NoticeType;
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
