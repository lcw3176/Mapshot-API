package com.mapshot.api.infra.exception.status;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode implements StatusCode {

    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    NO_AUTH_TOKEN(HttpStatus.UNAUTHORIZED, "허용되지 않는 접근입니다."),
    NOT_VALID_TOKEN(HttpStatus.FORBIDDEN, "잘못된 인증 토큰입니다."),
    NO_SUCH_NOTICE(HttpStatus.NOT_FOUND, "존재하지 않는 공지사항입니다."),
    NO_SUCH_USER(HttpStatus.NOT_FOUND, "존재하지 않는 유저입니다."),
    NO_SUCH_ALGORITHM(HttpStatus.NOT_FOUND, "암호화 알고리즘 탐색 불가"),
    NO_PRE_AUTH(HttpStatus.INTERNAL_SERVER_ERROR, "PreAuth 탐색 불가"),
    ;

    private final HttpStatus httpStatus;
    private final String message;

    private ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
