package com.mapshot.api.presentation.map.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LayerMapRequestTest {

    @Test
    void LayerMapRequest_생성_성공() {
        // when
        LayerMapRequest request = LayerMapRequest.builder()
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
    void LayerMapRequest_기본_생성자_테스트() {
        // when
        LayerMapRequest request = new LayerMapRequest();

        // then
        assertThat(request).isNotNull();
    }

    @Test
    void LayerMapRequest_전체_생성자_테스트() {
        // when
        LayerMapRequest request = new LayerMapRequest(37.5665f, 126.9780f, 15, "roadmap", false, false, "satellite");

        // then
        assertThat(request.getLat()).isEqualTo(37.5665f);
        assertThat(request.getLng()).isEqualTo(126.9780f);
        assertThat(request.getLevel()).isEqualTo(15);
        assertThat(request.getType()).isEqualTo("roadmap");
        assertThat(request.isLayerMode()).isFalse();
        assertThat(request.getLayer()).isEqualTo("satellite");
    }

    @Test
    void LayerMapRequest_Setter_테스트() {
        // given
        LayerMapRequest request = new LayerMapRequest();

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

