package com.mapshot.api.infra.web.auth.token;

import com.mapshot.api.infra.auth.token.TokenProcessor;
import com.mapshot.api.infra.exception.ApiException;
import com.mapshot.api.infra.exception.status.ErrorCode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class TokenProcessorTest {

    @Autowired
    private TokenProcessor tokenProcessor;

    private final String key = "sdfsefsdfsdf";

    @Test
    void 토큰_생성_테스트() {

        String token = tokenProcessor.makeToken(60, key);

        assertThatNoException().isThrownBy(() -> {
            tokenProcessor.isValid(key, token);
        });
    }

    @Test
    void 유효하지_않은_토큰이면_예외_발생() {
        String token = "hellohello";

        assertThatThrownBy(() -> tokenProcessor.isValid(key, token))
                .isInstanceOf(ApiException.class)
                .hasMessage(ErrorCode.NOT_VALID_TOKEN.getMessage());
    }

}
