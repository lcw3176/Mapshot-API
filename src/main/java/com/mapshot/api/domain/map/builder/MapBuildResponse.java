package com.mapshot.api.domain.map.builder;

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
