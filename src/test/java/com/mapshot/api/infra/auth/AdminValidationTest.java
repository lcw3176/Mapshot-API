package com.mapshot.api.infra.auth;

import com.mapshot.api.application.auth.Validation;
import com.mapshot.api.application.auth.TokenProcessor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AdminValidationTest {

    @Autowired
    private Validation adminValidation;

    @Autowired
    private TokenProcessor tokenProcessor;
    

    @Value("${jwt.admin.secret}")
    private String JWT_SECRET;


    @Test
    void 토큰_생성_테스트() {
        String token = adminValidation.makeToken();

        Assertions.assertTrue(tokenProcessor.isValid(JWT_SECRET, token));
    }
}