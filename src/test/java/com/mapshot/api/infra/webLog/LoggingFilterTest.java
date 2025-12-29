package com.mapshot.api.infra.webLog;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoggingFilterTest {

    private LoggingFilter loggingFilter;

    @Mock
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        loggingFilter = new LoggingFilter();
    }

    @Test
    void 필터_정상_동작() throws Exception {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/test");
        MockHttpServletResponse response = new MockHttpServletResponse();

        // when
        loggingFilter.doFilterInternal(request, response, filterChain);

        // then
        verify(filterChain, times(1)).doFilter(any(), any());
    }

    @Test
    void 필터_프로메테우스_엔드포인트_제외() throws Exception {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/actuator/prometheus");
        MockHttpServletResponse response = new MockHttpServletResponse();

        // when
        loggingFilter.doFilterInternal(request, response, filterChain);

        // then
        verify(filterChain, times(1)).doFilter(any(), any());
    }

    @Test
    void 필터_POST_요청_처리() throws Exception {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/test");
        request.setContentType("application/json");
        request.setContent("{\"key\":\"value\"}".getBytes());
        MockHttpServletResponse response = new MockHttpServletResponse();

        // when
        loggingFilter.doFilterInternal(request, response, filterChain);

        // then
        verify(filterChain, times(1)).doFilter(any(), any());
    }

    @Test
    void 필터_비동기_디스패치_처리() throws Exception {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/test");
        request.setAsyncSupported(true);
        MockHttpServletResponse response = new MockHttpServletResponse();

        // when
        loggingFilter.doFilterInternal(request, response, filterChain);

        // then
        verify(filterChain, times(1)).doFilter(any(), any());
    }
}

