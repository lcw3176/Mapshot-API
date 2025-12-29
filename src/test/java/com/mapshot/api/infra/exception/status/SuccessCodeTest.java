package com.mapshot.api.infra.exception.status;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

class SuccessCodeTest {

    @Test
    void SuccessCode_모든_값_존재_확인() {
        // when
        SuccessCode[] successCodes = SuccessCode.values();

        // then
        assertThat(successCodes).hasSize(1);
        assertThat(successCodes[0]).isEqualTo(SuccessCode.OK);
    }

    @Test
    void SuccessCode_OK_HTTP_상태코드_확인() {
        // when & then
        assertThat(SuccessCode.OK.getHttpStatus()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void SuccessCode_OK_메시지_확인() {
        // when & then
        assertThat(SuccessCode.OK.getMessage()).isEqualTo("ok");
    }

    @Test
    void SuccessCode_이름으로_찾기() {
        // when
        SuccessCode ok = SuccessCode.valueOf("OK");

        // then
        assertThat(ok).isEqualTo(SuccessCode.OK);
    }
}

