package com.mapshot.api.infra.exception.status;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

class ErrorCodeTest {

    @Test
    void ErrorCode_모든_값_존재_확인() {
        // when
        ErrorCode[] errorCodes = ErrorCode.values();

        // then
        assertThat(errorCodes.length).isGreaterThan(0);
    }

    @Test
    void ErrorCode_HTTP_상태코드_확인() {
        // when & then
        assertThat(ErrorCode.HANDLER_NOT_FOUND.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(ErrorCode.BAD_REQUEST.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(ErrorCode.NO_AUTH_TOKEN.getHttpStatus()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(ErrorCode.NOT_VALID_TOKEN.getHttpStatus()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(ErrorCode.NO_SUCH_NOTICE.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(ErrorCode.NO_SUCH_POST.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(ErrorCode.NOT_VALID_PASSWORD.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(ErrorCode.NO_SUCH_USER.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(ErrorCode.NO_SUCH_ALGORITHM.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(ErrorCode.NO_PRE_AUTH.getHttpStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(ErrorCode.EXTERNAL_API_FAILED.getHttpStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(ErrorCode.RATE_LIMIT_ACTIVATED.getHttpStatus()).isEqualTo(HttpStatus.TOO_MANY_REQUESTS);
    }

    @Test
    void ErrorCode_메시지_확인() {
        // when & then
        assertThat(ErrorCode.HANDLER_NOT_FOUND.getMessage()).isEqualTo("존재하지 않는 페이지입니다");
        assertThat(ErrorCode.BAD_REQUEST.getMessage()).isEqualTo("잘못된 요청입니다.");
        assertThat(ErrorCode.NO_AUTH_TOKEN.getMessage()).isEqualTo("허용되지 않는 접근입니다.");
        assertThat(ErrorCode.NOT_VALID_TOKEN.getMessage()).isEqualTo("잘못된 인증 토큰입니다.");
        assertThat(ErrorCode.NO_SUCH_NOTICE.getMessage()).isEqualTo("존재하지 않는 공지사항입니다.");
        assertThat(ErrorCode.NO_SUCH_POST.getMessage()).isEqualTo("존재하지 않는 게시글입니다.");
        assertThat(ErrorCode.NOT_VALID_PASSWORD.getMessage()).isEqualTo("잘못된 비밀번호입니다.");
        assertThat(ErrorCode.NO_SUCH_USER.getMessage()).isEqualTo("존재하지 않는 유저입니다.");
        assertThat(ErrorCode.NO_SUCH_ALGORITHM.getMessage()).isEqualTo("암호화 알고리즘 탐색 불가");
        assertThat(ErrorCode.NO_PRE_AUTH.getMessage()).isEqualTo("PreAuth 탐색 불가");
        assertThat(ErrorCode.EXTERNAL_API_FAILED.getMessage()).isEqualTo("외부 api 호출 에러");
        assertThat(ErrorCode.RATE_LIMIT_ACTIVATED.getMessage()).isEqualTo("레이트 리미터 호출 제한");
    }

    @Test
    void ErrorCode_이름으로_찾기() {
        // when
        ErrorCode handlerNotFound = ErrorCode.valueOf("HANDLER_NOT_FOUND");
        ErrorCode badRequest = ErrorCode.valueOf("BAD_REQUEST");
        ErrorCode noAuthToken = ErrorCode.valueOf("NO_AUTH_TOKEN");

        // then
        assertThat(handlerNotFound).isEqualTo(ErrorCode.HANDLER_NOT_FOUND);
        assertThat(badRequest).isEqualTo(ErrorCode.BAD_REQUEST);
        assertThat(noAuthToken).isEqualTo(ErrorCode.NO_AUTH_TOKEN);
    }
}

