package com.mapshot.api.common.token;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtUtilTest {

    @Test
    void 토큰_생성_테스트() {
        String token = JwtUtil.generate();

        assertTrue(JwtUtil.isValid(token));
    }

    @Test
    void 유효하지_않은_토큰이면_false_리턴() {
        String token = "hellohello";
        assertFalse(JwtUtil.isValid(token));
    }

}