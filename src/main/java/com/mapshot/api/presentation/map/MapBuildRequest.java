package com.mapshot.api.presentation.map;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class MapBuildRequest {

    // 요청한 지도 이미지의 중심 위도
    protected double lat;

    // 요청한 지도 이미지의 중심 경도
    protected double lng;

    // 영역의 북동쪽 위도
    protected double neLat;

    // 영역의 북동쪽 경도
    protected double neLng;

//    // 영역의 남서쪽 위도
//    protected double swLat;
//
//    // 영역의 남서쪽 경도
//    protected double swLng;

    // 지도 반경 값
    // ex) 1 -> 반경 1km의 지도
    protected int level;

    // 지도 생성 타입
    // ex) satellite_base -> 위성 사진 기반의 지도
    protected String type;


}
