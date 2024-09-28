package com.mapshot.api.infra.auth;

import com.mapshot.api.application.auth.Validation;
import com.mapshot.api.application.auth.token.TokenProcessor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;

import static org.assertj.core.api.Assertions.assertThatNoException;

@SpringBootTest
class AdminValidationTest {

    @Autowired
    private Validation adminValidation;

    @Autowired
    private TokenProcessor tokenProcessor;

    @Value("${jwt.admin.header}")
    private String ADMIN_HEADER_NAME;

    @Value("${jwt.admin.secret}")
    private String JWT_SECRET;


    @Test
    void 토큰_헤더_생성_테스트() {
        HttpHeaders headers = adminValidation.makeHeader();

        assertThatNoException()
                .isThrownBy(() -> tokenProcessor.isValid(JWT_SECRET, headers.getFirst(ADMIN_HEADER_NAME)));
    }


    @Test
    void 토큰_생성_테스트() {
        String token = adminValidation.makeToken();

        assertThatNoException()
                .isThrownBy(() -> tokenProcessor.isValid(JWT_SECRET, token));
    }
}