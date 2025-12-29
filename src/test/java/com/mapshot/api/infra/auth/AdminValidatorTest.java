package com.mapshot.api.infra.auth;

import com.mapshot.api.infra.exception.ApiException;
import com.mapshot.api.infra.exception.status.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class AdminValidatorTest {

    @Autowired
    private AdminValidator adminValidator;

    @Value("${jwt.admin.header}")
    private String ADMIN_SESSION;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpSession session;

    @BeforeEach
    void setUp() {
        when(request.getSession()).thenReturn(session);
    }

    @Test
    void 인증_성공_세션에_관리자_속성_존재() {
        // given
        when(session.getAttribute(ADMIN_SESSION)).thenReturn(true);

        // when & then
        assertDoesNotThrow(() -> adminValidator.isValidOrThrowException(request));
    }

    @Test
    void 인증_실패_세션이_null() {
        // given
        when(request.getSession()).thenReturn(null);

        // when & then
        assertThatThrownBy(() -> adminValidator.isValidOrThrowException(request))
                .isInstanceOf(ApiException.class)
                .hasMessage(ErrorCode.NO_AUTH_TOKEN.getMessage());
    }

    @Test
    void 인증_실패_세션에_관리자_속성_없음() {
        // given
        when(session.getAttribute(ADMIN_SESSION)).thenReturn(null);

        // when & then
        assertThatThrownBy(() -> adminValidator.isValidOrThrowException(request))
                .isInstanceOf(ApiException.class)
                .hasMessage(ErrorCode.NO_AUTH_TOKEN.getMessage());
    }

    @Test
    void 인증_실패_세션에_관리자_속성이_false() {
        // given
        when(session.getAttribute(ADMIN_SESSION)).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> adminValidator.isValidOrThrowException(request))
                .isInstanceOf(ApiException.class)
                .hasMessage(ErrorCode.NO_AUTH_TOKEN.getMessage());
    }
}

