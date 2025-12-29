package com.mapshot.api.infra.client;

import com.mapshot.api.infra.exception.ApiException;
import com.mapshot.api.infra.exception.status.ErrorCode;
import com.mapshot.api.infra.exception.status.StatusCode;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ApiHandlerTest {

    @Test
    void 핸들러_성공_정상_반환() {
        // given
        String expected = "success";

        // when
        String result = ApiHandler.handle(() -> expected);

        // then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void 핸들러_성공_정수_반환() {
        // given
        int expected = 42;

        // when
        int result = ApiHandler.handle(() -> expected);

        // then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void 핸들러_실패_RuntimeException_발생시_ApiException_변환() {
        // given
        RuntimeException runtimeException = new ApiException(ErrorCode.EXTERNAL_API_FAILED);

        // when & then
        assertThatThrownBy(() -> ApiHandler.handle(() -> {
            throw runtimeException;
        }))
                .isInstanceOf(ApiException.class)
                .hasMessage(ErrorCode.EXTERNAL_API_FAILED.getMessage())
                .hasCause(runtimeException);
    }


    @Test
    void 핸들러_성공_null_반환() {
        // when
        String result = ApiHandler.handle(() -> null);

        // then
        assertThat(result).isNull();
    }

    @Test
    void 핸들러_성공_복잡한_객체_반환() {
        // given
        TestObject expected = new TestObject("test", 123);

        // when
        TestObject result = ApiHandler.handle(() -> expected);

        // then
        assertThat(result).isEqualTo(expected);
    }

    // 테스트용 클래스
    static class TestObject {
        private String name;
        private int value;

        public TestObject(String name, int value) {
            this.name = name;
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TestObject that = (TestObject) o;
            return value == that.value && name.equals(that.name);
        }
    }
}

