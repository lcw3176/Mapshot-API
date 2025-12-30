package com.mapshot.api.infra.exception;

import com.mapshot.api.infra.client.slack.SlackClient;
import com.mapshot.api.infra.exception.status.ErrorCode;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @Mock
    private SlackClient slackClient;

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Test
    void ConstraintViolationException_처리() {
        // given
        ConstraintViolationException exception = mock(ConstraintViolationException.class);

        // when
        globalExceptionHandler.violationExceptionHandler(exception);

        // then
        verify(slackClient, never()).sendMessage(any());
    }

    @Test
    void MethodArgumentNotValidException_처리() {
        // given
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);

        // when
        globalExceptionHandler.violationExceptionHandler(exception);

        // then
        verify(slackClient, never()).sendMessage(any());
    }

    @Test
    void MethodArgumentTypeMismatchException_처리() {
        // given
        MethodArgumentTypeMismatchException exception = mock(MethodArgumentTypeMismatchException.class);

        // when
        globalExceptionHandler.violationExceptionHandler(exception);

        // then
        verify(slackClient, never()).sendMessage(any());
    }

    @Test
    void HttpRequestMethodNotSupportedException_처리() {
        // given
        HttpRequestMethodNotSupportedException exception = mock(HttpRequestMethodNotSupportedException.class);

        // when
        globalExceptionHandler.violationExceptionHandler(exception);

        // then
        verify(slackClient, never()).sendMessage(any());
    }

    @Test
    void ClassCastException_처리() {
        // given
        ClassCastException exception = new ClassCastException("test");

        // when
        globalExceptionHandler.botExceptionHandler(exception);

        // then
        verify(slackClient, never()).sendMessage(any());
    }

    @Test
    void NoHandlerFoundException_처리() {
        // given
        NoHandlerFoundException exception = new NoHandlerFoundException("GET", "/test", null);

        // when
        ResponseEntity<String> response = globalExceptionHandler.notFoundExceptionHandler(exception);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo(ErrorCode.HANDLER_NOT_FOUND.getMessage());
        verify(slackClient, never()).sendMessage(any());
    }

    @Test
    void ApiException_처리() {
        // given
        ApiException exception = new ApiException(ErrorCode.NO_SUCH_NOTICE);

        // when
        ResponseEntity<String> response = globalExceptionHandler.apiExceptionHandler(exception);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo(ErrorCode.NO_SUCH_NOTICE.getMessage());
        verify(slackClient, never()).sendMessage(any());
    }

    @Test
    void 일반_Exception_처리() {
        // given
        Exception exception = new RuntimeException("테스트 예외");
        doNothing().when(slackClient).sendMessage(any(Throwable.class));

        // when
        globalExceptionHandler.exceptionHandler(exception);

        // then
        verify(slackClient, times(1)).sendMessage(any(Throwable.class));
    }

    @Test
    void 일반_Exception_처리_다양한_예외_타입() {
        // given
        Exception exception1 = new NullPointerException("NPE");
        Exception exception2 = new IllegalStateException("ISE");
        Exception exception3 = new IllegalArgumentException("IAE");
        doNothing().when(slackClient).sendMessage(any(Throwable.class));

        // when
        globalExceptionHandler.exceptionHandler(exception1);
        globalExceptionHandler.exceptionHandler(exception2);
        globalExceptionHandler.exceptionHandler(exception3);

        // then
        verify(slackClient, times(3)).sendMessage(any(Throwable.class));
    }
}

