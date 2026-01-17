package com.mapshot.api.presentation.map.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class KakaoMapRequestTest {

    @Test
    void KakaoMapRequest_생성_성공() {
        // when
        KakaoMapRequest request = KakaoMapRequest.builder()
                .lat(37.5665f)
                .lng(126.9780f)
                .level(15)
                .type("roadmap")
                .layerMode(false)
                .layer("satellite")
                .topography(false)
                .build();

        // then
        assertThat(request.getLat()).isEqualTo(37.5665f);
        assertThat(request.getLng()).isEqualTo(126.9780f);
        assertThat(request.getLevel()).isEqualTo(15);
        assertThat(request.getType()).isEqualTo("roadmap");
        assertThat(request.isLayerMode()).isFalse();
        assertThat(request.getLayer()).isEqualTo("satellite");
    }

    @Test
    void KakaoMapRequest_기본_생성자_테스트() {
        // when
        KakaoMapRequest request = new KakaoMapRequest();

        // then
        assertThat(request).isNotNull();
    }

    @Test
    void KakaoMapRequest_전체_생성자_테스트() {
        // when
        KakaoMapRequest request = new KakaoMapRequest(37.5665f, 126.9780f, 15, "roadmap", false, false, "satellite");

        // then
        assertThat(request.getLat()).isEqualTo(37.5665f);
        assertThat(request.getLng()).isEqualTo(126.9780f);
        assertThat(request.getLevel()).isEqualTo(15);
        assertThat(request.getType()).isEqualTo("roadmap");
        assertThat(request.isLayerMode()).isFalse();
        assertThat(request.getLayer()).isEqualTo("satellite");
    }

    @Test
    void KakaoMapRequest_Setter_테스트() {
        // given
        KakaoMapRequest request = new KakaoMapRequest();

        // when
        request.setLat(37.5665f);
        request.setLng(126.9780f);
        request.setLevel(15);
        request.setType("roadmap");
        request.setLayerMode(true);
        request.setLayer("satellite");

        // then
        assertThat(request.getLat()).isEqualTo(37.5665f);
        assertThat(request.getLng()).isEqualTo(126.9780f);
        assertThat(request.getLevel()).isEqualTo(15);
        assertThat(request.getType()).isEqualTo("roadmap");
        assertThat(request.isLayerMode()).isTrue();
        assertThat(request.getLayer()).isEqualTo("satellite");
    }
}

