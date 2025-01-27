package com.mapshot.api.presentation.map.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoogleTemplateRequest {

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
    private boolean noLabel;

}
