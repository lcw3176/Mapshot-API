package com.mapshot.api.presentation.admin;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


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
    private String content;

}
