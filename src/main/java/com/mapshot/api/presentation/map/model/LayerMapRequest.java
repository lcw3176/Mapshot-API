package com.mapshot.api.presentation.map.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LayerMapRequest {
    private float lat;
    private float lng;
    private int level;
    private String type;
    private boolean layerMode;
    private String layer;
}
