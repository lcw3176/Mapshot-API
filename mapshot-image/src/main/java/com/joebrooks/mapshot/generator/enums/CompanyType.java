package com.joebrooks.mapshot.generator.enums;

import lombok.Getter;

@Getter
public enum CompanyType {
    kakao(new MapRadius[]{
            KakaoMapRadius.ONE,
            KakaoMapRadius.TWO,
            KakaoMapRadius.FIVE,
            KakaoMapRadius.TEN});

    private final MapRadius[] mapRadius;

    private CompanyType(MapRadius[] mapRadius) {
        this.mapRadius = mapRadius;
    }
}