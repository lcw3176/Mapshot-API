package com.mapshot.api.presentation.map.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GoogleMapRequestTest {

    @Test
    void GoogleMapRequest_생성_성공() {
        // when
        GoogleMapRequest request = GoogleMapRequest.builder()
                .lat(37.5665f)
                .lng(126.9780f)
                .level(15)
                .type("roadmap")
                .noLabel(false)
                .layer("satellite")
                .topography(false)
                .build();

        // then
        assertThat(request.getLat()).isEqualTo(37.5665f);
        assertThat(request.getLng()).isEqualTo(126.9780f);
        assertThat(request.getLevel()).isEqualTo(15);
        assertThat(request.getType()).isEqualTo("roadmap");
        assertThat(request.isNoLabel()).isFalse();
        assertThat(request.getLayer()).isEqualTo("satellite");
    }

    @Test
    void GoogleMapRequest_기본_생성자_테스트() {
        // when
        GoogleMapRequest request = new GoogleMapRequest();

        // then
        assertThat(request).isNotNull();
    }

    @Test
    void GoogleMapRequest_전체_생성자_테스트() {
        // when
        GoogleMapRequest request = new GoogleMapRequest(37.5665f, 126.9780f, 15, "roadmap", false, false, "satellite");

        // then
        assertThat(request.getLat()).isEqualTo(37.5665f);
        assertThat(request.getLng()).isEqualTo(126.9780f);
        assertThat(request.getLevel()).isEqualTo(15);
        assertThat(request.getType()).isEqualTo("roadmap");
        assertThat(request.isNoLabel()).isFalse();
        assertThat(request.getLayer()).isEqualTo("satellite");
    }

    @Test
    void GoogleMapRequest_Setter_테스트() {
        // given
        GoogleMapRequest request = new GoogleMapRequest();

        // when
        request.setLat(37.5665f);
        request.setLng(126.9780f);
        request.setLevel(15);
        request.setType("roadmap");
        request.setNoLabel(true);
        request.setLayer("satellite");
        request.setTopography(false);

        // then
        assertThat(request.getLat()).isEqualTo(37.5665f);
        assertThat(request.getLng()).isEqualTo(126.9780f);
        assertThat(request.getLevel()).isEqualTo(15);
        assertThat(request.getType()).isEqualTo("roadmap");
        assertThat(request.isNoLabel()).isTrue();
        assertThat(request.getLayer()).isEqualTo("satellite");
    }
}

