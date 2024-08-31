package com.mapshot.api.presentation.map;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class KakaoMapRequest extends MapBuildRequest {

    // 도시 계획 레이어 적용 여부
    private boolean layerMode;

}
