package com.mapshot.api.infra.auth.token;

import com.mapshot.api.application.auth.token.TokenProcessor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TokenProcessorTest {

    private final String key = "sdfsefsdfsdf";
    @Autowired
    private TokenProcessor tokenProcessor;

    @Test
    void 토큰_생성_테스트() {

        String token = tokenProcessor.makeToken(60, key);

        Assertions.assertTrue(tokenProcessor.isValid(key, token));
    }

    @Test
    void 유효하지_않은_토큰이면_false_리턴() {
        String token = "hellohello";

        Assertions.assertFalse(tokenProcessor.isValid(key, token));
    }

}
