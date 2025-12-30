package com.mapshot.api.infra.exception;

import com.mapshot.api.infra.exception.status.ErrorCode;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ApiExceptionTest {

    @Test
    void ApiException_생성_StatusCode만_사용() {
        // given
        ErrorCode errorCode = ErrorCode.NO_SUCH_NOTICE;

        // when
        ApiException exception = new ApiException(errorCode);

        // then
        assertThat(exception.getCode()).isEqualTo(errorCode);
        assertThat(exception.getMessage()).isEqualTo(errorCode.getMessage());
    }

    @Test
    void ApiException_생성_원인_포함() {
        // given
        ErrorCode errorCode = ErrorCode.NO_SUCH_NOTICE;
        Throwable cause = new RuntimeException("원인 예외");

        // when
        ApiException exception = new ApiException(errorCode, cause);

        // then
        assertThat(exception.getCode()).isEqualTo(errorCode);
        assertThat(exception.getCause()).isEqualTo(cause);
    }

    @Test
    void ApiException_다양한_ErrorCode_테스트() {
        // when & then
        for (ErrorCode errorCode : ErrorCode.values()) {
            ApiException exception = new ApiException(errorCode);
            assertThat(exception.getCode()).isEqualTo(errorCode);
            assertThat(exception.getMessage()).isEqualTo(errorCode.getMessage());
        }
    }

    @Test
    void ApiException_원인_예외_다양한_타입() {
        // given
        ErrorCode errorCode = ErrorCode.EXTERNAL_API_FAILED;
        Throwable[] causes = {
                new RuntimeException("RuntimeException"),
                new NullPointerException("NullPointerException"),
                new IllegalStateException("IllegalStateException"),
                new IllegalArgumentException("IllegalArgumentException")
        };

        // when & then
        for (Throwable cause : causes) {
            ApiException exception = new ApiException(errorCode, cause);
            assertThat(exception.getCode()).isEqualTo(errorCode);
            assertThat(exception.getCause()).isEqualTo(cause);
        }
    }
}

