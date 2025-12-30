package com.mapshot.api.infra.auth;

import com.mapshot.api.infra.exception.ApiException;
import com.mapshot.api.infra.exception.status.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.web.method.HandlerMethod;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class AuthInterceptorTest {

    @Autowired
    private AuthInterceptor authInterceptor;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HandlerMethod handlerMethod;

    @BeforeEach
    void setUp() {
        when(request.getMethod()).thenReturn(HttpMethod.GET.toString());
    }


    @Test
    void Preflight_요청은_통과() throws Exception {
        // given
        when(request.getMethod()).thenReturn(HttpMethod.OPTIONS.toString());
        when(request.getHeader("Access-Control-Request-Headers")).thenReturn("Content-Type");
        when(request.getHeader("Access-Control-Request-Method")).thenReturn("POST");
        when(request.getHeader("Origin")).thenReturn("http://localhost:3000");

        // when
        boolean result = authInterceptor.preHandle(request, response, new Object());

        // then
        assertTrue(result);
    }


    @Test
    void HandlerMethod가_아닌_경우_예외_발생() {
        // when & then
        assertThatThrownBy(() -> authInterceptor.preHandle(request, response, new Object()))
                .isInstanceOf(ApiException.class)
                .hasMessage(ErrorCode.HANDLER_NOT_FOUND.getMessage());
    }

    // 테스트용 컨트롤러
    static class TestController {
        public void noAuthMethod() {
        }

        @PreAuth(Accessible.ADMIN)
        public void adminMethod() {
        }
    }
}

