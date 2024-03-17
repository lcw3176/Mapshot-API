package com.mapshot.api.presentation.map.builder.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MapBuildResponse {

    private int x;
    private int y;
    private String uuid;
}
