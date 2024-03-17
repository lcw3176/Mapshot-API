package com.mapshot.api.infra.client.lambda.model;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class LambdaRequest {
    // 도시 계획 레이어 적용 여부
    private boolean layerMode;

    // 요청한 지도 이미지의 중심 위도
    private double lat;

    // 요청한 지도 이미지의 중심 경도
    private double lng;

    // 지도 반경 값
    // ex) 1 -> 반경 1km의 지도
    private int level;

    // 지도 생성 타입
    // ex) satellite_base -> 위성 사진 기반의 지도
    private String type;

    // 지도 생성 회사
    private String companyType;
}
