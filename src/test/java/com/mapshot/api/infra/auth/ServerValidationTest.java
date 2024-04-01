package com.mapshot.api.infra.auth;

import com.mapshot.api.infra.auth.token.TokenProcessor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;

import static org.assertj.core.api.Assertions.assertThatNoException;

@SpringBootTest
class ServerValidationTest {

    @Autowired
    private Validation serverValidation;

    @Autowired
    private TokenProcessor tokenProcessor;

    @Value("${jwt.image.header}")
    private String SERVER_HEADER_NAME;

    @Value("${jwt.image.secret}")
    private String JWT_SECRET;


    @Test
    void 토큰_헤더_생성_테스트() {
        HttpHeaders headers = serverValidation.makeHeader();

        assertThatNoException()
                .isThrownBy(() -> tokenProcessor.isValid(JWT_SECRET, headers.getFirst(SERVER_HEADER_NAME)));
    }


    @Test
    void 토큰_생성_테스트() {
        String token = serverValidation.makeToken();

        assertThatNoException()
                .isThrownBy(() -> tokenProcessor.isValid(JWT_SECRET, token));
    }
}