package com.mapshot.api.common.token;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.mapshot.api.image.token.JwtProvider;
import org.junit.jupiter.api.Test;

class JwtProviderTest {

    @Test
    void 토큰_생성_테스트() {
        String token = JwtProvider.generate();

        assertTrue(JwtProvider.isValid(token));
    }

    @Test
    void 유효하지_않은_토큰이면_false_리턴() {
        String token = "hellohello";
        assertFalse(JwtProvider.isValid(token));
    }

}