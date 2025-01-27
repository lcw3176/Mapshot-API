package com.mapshot.api.presentation.map.model;

import com.mapshot.api.domain.community.comment.CommentConst;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KakaoTemplateRequest {

    @NotBlank
    private float lat;

    @NotBlank
    private float lng;

    @NotBlank
    @Positive
    private int level;

    @NotBlank
    private String type;

    @NotBlank
    private boolean layerMode;

}
