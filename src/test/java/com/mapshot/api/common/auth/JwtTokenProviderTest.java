package com.mapshot.api.common.auth;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class JwtTokenProviderTest {

    @Test
    void 토큰_생성_테스트() {
        String token = JwtTokenProvider.generate();

        assertTrue(JwtTokenProvider.isValid(token));
    }

    @Test
    void 유효하지_않은_토큰이면_false_리턴() {
        String token = "hellohello";
        assertFalse(JwtTokenProvider.isValid(token));
    }

}