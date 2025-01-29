package com.mapshot.api.presentation.map.model;

import lombok.Getter;

@Getter
public abstract class MapRequest {
    private float lat;
    private float lng;
    private int level;
    private String baseMap;
}
