package com.mapshot.api.common.validation.token;

import com.mapshot.api.common.exception.ApiException;
import com.mapshot.api.common.exception.status.ErrorCode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class ImageTokenTest {

    @Autowired
    private ImageToken imageToken;

    @Test
    void 토큰_생성_테스트() {
        String token = imageToken.generate();

        assertThatNoException().isThrownBy(() -> {
            imageToken.isValid(token);
        });
    }

    @Test
    void 유효하지_않은_토큰이면_예외_발생() {
        String token = "hellohello";

        assertThatThrownBy(() -> imageToken.isValid(token))
                .isInstanceOf(ApiException.class)
                .hasMessage(ErrorCode.NOT_VALID_TOKEN.getMessage());
    }

}