package com.mapshot.api.presentation.map;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class GoogleMapRequest extends MapBuildRequest {

    // 일반지도 지형지물 표시 여부
    private boolean noLabel;

}
