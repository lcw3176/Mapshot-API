package com.mapshot.api.presentation.map.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoogleMapRequest {
    private float lat;
    private float lng;
    private int level;
    private String type;
    private boolean noLabel;
}
