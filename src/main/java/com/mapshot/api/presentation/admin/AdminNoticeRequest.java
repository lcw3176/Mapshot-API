package com.mapshot.api.presentation.admin;

import com.mapshot.api.domain.notice.NoticeConst;
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
public class AdminNoticeRequest {

    @NotBlank
    private String noticeType;

    @NotBlank
    private String title;

    @NotBlank
    @Length(max = NoticeConst.MAX_CONTENT_LENGTH)
    private String content;

}
