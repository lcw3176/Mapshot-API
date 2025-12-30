package com.mapshot.api.infra.rateLimit;

import com.mapshot.api.infra.exception.ApiException;
import com.mapshot.api.infra.exception.status.ErrorCode;
import io.github.bucket4j.Bucket;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class RateLimitInterceptorTest {

    @Autowired
    private RateLimitInterceptor rateLimitInterceptor;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Autowired
    private Bucket bucket;

    @BeforeEach
    void setUp() {
        bucket.reset();
    }

    @Test
    void 레이트_리미트_통과_성공() throws Exception {
        // given
        Object handler = new Object();

        // when
        boolean result = rateLimitInterceptor.preHandle(request, response, handler);

        // then
        assertTrue(result);
    }

    @Test
    void 레이트_리미트_초과시_예외_발생() throws Exception {
        // given
        Object handler = new Object();
        bucket.tryConsumeAsMuchAsPossible();

        // when & then
        assertThatThrownBy(() -> rateLimitInterceptor.preHandle(request, response, handler))
                .isInstanceOf(ApiException.class)
                .hasMessage(ErrorCode.RATE_LIMIT_ACTIVATED.getMessage());
    }

    @Test
    void 토큰_소진_후_예외_발생() throws Exception {
        // given
        Object handler = new Object();
        // 버킷의 모든 토큰 소진
        while (bucket.tryConsume(1)) {
            // 토큰 소진
        }

        // when & then
        assertThatThrownBy(() -> rateLimitInterceptor.preHandle(request, response, handler))
                .isInstanceOf(ApiException.class)
                .hasMessage(ErrorCode.RATE_LIMIT_ACTIVATED.getMessage());
    }
}

