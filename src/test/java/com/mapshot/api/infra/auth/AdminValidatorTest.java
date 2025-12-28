package com.mapshot.api.infra.auth;

import com.mapshot.api.infra.exception.ApiException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AdminValidator 테스트")
class AdminValidatorTest {

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private AdminValidator adminValidator;

    private static final String JWT_SECRET = "test-secret-key";
    private static final String ADMIN_HEADER_NAME = "X-Admin-Token";
    private static final String VALID_TOKEN = "valid-jwt-token";

    @BeforeEach
    void setUp() {
        // 리플렉션을 사용하여 private 필드 주입
        ReflectionTestUtils.setField(adminValidator, "JWT_SECRET", JWT_SECRET);
        ReflectionTestUtils.setField(adminValidator, "ADMIN_HEADER_NAME", ADMIN_HEADER_NAME);
    }

    // ================== 쿠키 관련 테스트 ==================

    @Test
    @DisplayName("쿠키 없음 - null 반환")
    void cookies_is_null_throws_no_auth_token_exception() {
        // Given
        when(request.getCookies()).thenReturn(null);

        // When & Then
        assertThatThrownBy(() -> adminValidator.isValidOrThrowException(request))
                .isInstanceOf(ApiException.class);
    }

    @Test
    @DisplayName("쿠키 없음 - 빈 배열 반환")
    void cookies_is_empty_array_throws_no_auth_token_exception() {
        // Given
        when(request.getCookies()).thenReturn(new Cookie[]{});

        // When & Then
        assertThatThrownBy(() -> adminValidator.isValidOrThrowException(request))
                .isInstanceOf(ApiException.class);
    }

    @Test
    @DisplayName("쿠키 존재 - 다른 이름의 쿠키만 있을 때")
    void cookies_exist_but_admin_token_not_found() {
        // Given
        Cookie cookie1 = new Cookie("JSESSIONID", "session-id");
        Cookie cookie2 = new Cookie("other-cookie", "other-value");
        when(request.getCookies()).thenReturn(new Cookie[]{cookie1, cookie2});

        // When & Then
        assertThatThrownBy(() -> adminValidator.isValidOrThrowException(request))
                .isInstanceOf(ApiException.class);
    }

    // ================== 정상 토큰 검증 테스트 ==================

    @Test
    @DisplayName("정상 - 유효한 토큰으로 검증 성공")
    void valid_token_passes_validation() {
        // Given
        Cookie adminCookie = new Cookie(ADMIN_HEADER_NAME, VALID_TOKEN);
        Cookie otherCookie = new Cookie("JSESSIONID", "session-id");
        when(request.getCookies()).thenReturn(new Cookie[]{otherCookie, adminCookie});
        when(tokenProvider.isValid(JWT_SECRET, VALID_TOKEN)).thenReturn(true);

        // When & Then
        adminValidator.isValidOrThrowException(request);

        verify(tokenProvider).isValid(JWT_SECRET, VALID_TOKEN);
    }

    @Test
    @DisplayName("정상 - 여러 쿠키 중 올바른 토큰 찾아서 검증")
    void find_valid_token_among_multiple_cookies() {
        // Given
        Cookie cookie1 = new Cookie("cookie1", "value1");
        Cookie cookie2 = new Cookie(ADMIN_HEADER_NAME, VALID_TOKEN);
        Cookie cookie3 = new Cookie("cookie3", "value3");
        when(request.getCookies()).thenReturn(new Cookie[]{cookie1, cookie2, cookie3});
        when(tokenProvider.isValid(JWT_SECRET, VALID_TOKEN)).thenReturn(true);

        // When & Then
        adminValidator.isValidOrThrowException(request);

        verify(tokenProvider).isValid(JWT_SECRET, VALID_TOKEN);
    }

    // ================== 토큰 검증 실패 테스트 ==================

    @Test
    @DisplayName("토큰 검증 실패 - isValid 메서드가 false 반환")
    void invalid_token_throws_no_auth_token_exception() {
        // Given
        Cookie adminCookie = new Cookie(ADMIN_HEADER_NAME, "invalid-token");
        when(request.getCookies()).thenReturn(new Cookie[]{adminCookie});
        when(tokenProvider.isValid(JWT_SECRET, "invalid-token")).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> adminValidator.isValidOrThrowException(request))
                .isInstanceOf(ApiException.class);

        verify(tokenProvider).isValid(JWT_SECRET, "invalid-token");
    }

    @Test
    @DisplayName("토큰 검증 실패 - 만료된 토큰")
    void expired_token_throws_no_auth_token_exception() {
        // Given
        String expiredToken = "expired-jwt-token";
        Cookie adminCookie = new Cookie(ADMIN_HEADER_NAME, expiredToken);
        when(request.getCookies()).thenReturn(new Cookie[]{adminCookie});
        when(tokenProvider.isValid(JWT_SECRET, expiredToken)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> adminValidator.isValidOrThrowException(request))
                .isInstanceOf(ApiException.class);
    }

    @Test
    @DisplayName("토큰 검증 실패 - 손상된 토큰")
    void malformed_token_throws_no_auth_token_exception() {
        // Given
        String malformedToken = "malformed-token";
        Cookie adminCookie = new Cookie(ADMIN_HEADER_NAME, malformedToken);
        when(request.getCookies()).thenReturn(new Cookie[]{adminCookie});
        when(tokenProvider.isValid(JWT_SECRET, malformedToken)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> adminValidator.isValidOrThrowException(request))
                .isInstanceOf(ApiException.class);
    }

    // ================== 엣지 케이스 테스트 ==================

    @Test
    @DisplayName("엣지 케이스 - 빈 토큰 값")
    void empty_token_value_throws_no_auth_token_exception() {
        // Given
        Cookie adminCookie = new Cookie(ADMIN_HEADER_NAME, "");
        when(request.getCookies()).thenReturn(new Cookie[]{adminCookie});
        when(tokenProvider.isValid(JWT_SECRET, "")).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> adminValidator.isValidOrThrowException(request))
                .isInstanceOf(ApiException.class);
    }

    @Test
    @DisplayName("엣지 케이스 - null 토큰 값")
    void null_token_value_throws_no_auth_token_exception() {
        // Given
        Cookie adminCookie = new Cookie(ADMIN_HEADER_NAME, null);
        when(request.getCookies()).thenReturn(new Cookie[]{adminCookie});
        when(tokenProvider.isValid(JWT_SECRET, null)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> adminValidator.isValidOrThrowException(request))
                .isInstanceOf(ApiException.class);
    }

    @Test
    @DisplayName("엣지 케이스 - 쿠키 이름이 대소문자 다를 때")
    void cookie_name_case_sensitive() {
        // Given: ADMIN_HEADER_NAME = "X-Admin-Token" 인데 "x-admin-token" 으로 요청
        Cookie adminCookie = new Cookie("x-admin-token", VALID_TOKEN);
        when(request.getCookies()).thenReturn(new Cookie[]{adminCookie});

        // When & Then
        assertThatThrownBy(() -> adminValidator.isValidOrThrowException(request))
                .isInstanceOf(ApiException.class);
    }

    // ================== 긴 토큰 값 테스트 ==================

    @Test
    @DisplayName("긴 토큰 값 - 매우 긴 JWT 토큰")
    void very_long_token_passes_validation() {
        // Given
        String longToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c" + "extra_long_content".repeat(10);
        Cookie adminCookie = new Cookie(ADMIN_HEADER_NAME, longToken);
        when(request.getCookies()).thenReturn(new Cookie[]{adminCookie});
        when(tokenProvider.isValid(JWT_SECRET, longToken)).thenReturn(true);

        // When & Then
        adminValidator.isValidOrThrowException(request);

        verify(tokenProvider).isValid(JWT_SECRET, longToken);
    }

    // ================== 설정값 관련 테스트 ==================

    @Test
    @DisplayName("설정값 - JWT_SECRET이 올바르게 전달되는지 확인")
    void jwt_secret_is_passed_correctly_to_token_provider() {
        // Given
        String customSecret = "custom-secret";
        ReflectionTestUtils.setField(adminValidator, "JWT_SECRET", customSecret);

        Cookie adminCookie = new Cookie(ADMIN_HEADER_NAME, VALID_TOKEN);
        when(request.getCookies()).thenReturn(new Cookie[]{adminCookie});
        when(tokenProvider.isValid(customSecret, VALID_TOKEN)).thenReturn(true);

        // When
        adminValidator.isValidOrThrowException(request);

        // Then
        verify(tokenProvider).isValid(customSecret, VALID_TOKEN);
    }

    @Test
    @DisplayName("설정값 - ADMIN_HEADER_NAME이 올바르게 적용되는지 확인")
    void admin_header_name_is_applied_correctly() {
        // Given
        String customHeaderName = "Custom-Admin-Header";
        ReflectionTestUtils.setField(adminValidator, "ADMIN_HEADER_NAME", customHeaderName);

        Cookie adminCookie = new Cookie(customHeaderName, VALID_TOKEN);
        Cookie otherCookie = new Cookie(ADMIN_HEADER_NAME, "wrong-token");
        when(request.getCookies()).thenReturn(new Cookie[]{otherCookie, adminCookie});
        when(tokenProvider.isValid(JWT_SECRET, VALID_TOKEN)).thenReturn(true);

        // When
        adminValidator.isValidOrThrowException(request);

        // Then
        verify(tokenProvider).isValid(JWT_SECRET, VALID_TOKEN);
    }

    // ================== TokenProvider 예외 처리 테스트 ==================

    @Test
    @DisplayName("예외 - TokenProvider.isValid()에서 예외 발생")
    void token_provider_throws_exception() {
        // Given
        Cookie adminCookie = new Cookie(ADMIN_HEADER_NAME, VALID_TOKEN);
        when(request.getCookies()).thenReturn(new Cookie[]{adminCookie});
        when(tokenProvider.isValid(JWT_SECRET, VALID_TOKEN))
                .thenThrow(new RuntimeException("Token validation error"));

        // When & Then
        assertThatThrownBy(() -> adminValidator.isValidOrThrowException(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Token validation error");
    }

    // ================== 인터페이스 구현 확인 ==================

    @Test
    @DisplayName("인터페이스 - Validator 인터페이스 구현 확인")
    void admin_validator_implements_validator_interface() {
        // Given & Then
        assertThat(adminValidator).isInstanceOf(Validator.class);
    }
}