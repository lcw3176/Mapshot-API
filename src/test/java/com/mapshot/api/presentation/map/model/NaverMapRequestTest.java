package com.mapshot.api.presentation.map.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NaverMapRequestTest {

    @Test
    void NaverMapRequest_생성_성공() {
        // when
        NaverMapRequest request = NaverMapRequest.builder()
                .lat(37.5665f)
                .lng(126.9780f)
                .level(15)
                .type("roadmap")
                .layer("satellite")
                .topography(false)
                .build();

        // then
        assertThat(request.getLat()).isEqualTo(37.5665f);
        assertThat(request.getLng()).isEqualTo(126.9780f);
        assertThat(request.getLevel()).isEqualTo(15);
        assertThat(request.getType()).isEqualTo("roadmap");
        assertThat(request.getLayer()).isEqualTo("satellite");
    }

    @Test
    void NaverMapRequest_기본_생성자_테스트() {
        // when
        NaverMapRequest request = new NaverMapRequest();

        // then
        assertThat(request).isNotNull();
    }

    @Test
    void NaverMapRequest_전체_생성자_테스트() {
        // when
        NaverMapRequest request = new NaverMapRequest(37.5665f, 126.9780f, 15, "roadmap", false, "satellite");

        // then
        assertThat(request.getLat()).isEqualTo(37.5665f);
        assertThat(request.getLng()).isEqualTo(126.9780f);
        assertThat(request.getLevel()).isEqualTo(15);
        assertThat(request.getType()).isEqualTo("roadmap");
        assertThat(request.getLayer()).isEqualTo("satellite");
    }

    @Test
    void NaverMapRequest_Setter_테스트() {
        // given
        NaverMapRequest request = new NaverMapRequest();

        // when
        request.setLat(37.5665f);
        request.setLng(126.9780f);
        request.setLevel(15);
        request.setType("roadmap");
        request.setLayer("satellite");

        // then
        assertThat(request.getLat()).isEqualTo(37.5665f);
        assertThat(request.getLng()).isEqualTo(126.9780f);
        assertThat(request.getLevel()).isEqualTo(15);
        assertThat(request.getType()).isEqualTo("roadmap");
        assertThat(request.getLayer()).isEqualTo("satellite");
    }
}

