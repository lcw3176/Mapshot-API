package com.mapshot.api.presentation.map.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KakaoMapRequest extends MapRequest {

    private boolean layerMode;
}
