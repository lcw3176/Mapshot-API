package com.mapshot.api.presentation.map.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NaverMapRequest {
    private float lat;
    private float lng;
    private int level;
    private String type;
}
