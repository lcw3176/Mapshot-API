package com.mapshot.api.domain.coord;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CoordTest {

    @Test
    void 좌표_엔티티_생성_성공() {
        // when
        Coord coord = Coord.builder().build();

        // then
        assertThat(coord).isNotNull();
    }

    @Test
    void 좌표_빌더_패턴_테스트() {
        // when
        Coord coord = Coord.builder().build();

        // then
        assertThat(coord).isNotNull();
    }
}

