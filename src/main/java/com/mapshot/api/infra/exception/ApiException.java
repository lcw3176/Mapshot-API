package com.mapshot.api.infra.exception;

import com.mapshot.api.infra.exception.status.StatusCode;
import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {

    private final StatusCode code;

    public ApiException(StatusCode code) {
        super(code.getMessage());
        this.code = code;
    }

    public ApiException(StatusCode code, Throwable cause) {
        super(code.getMessage(), cause);
        this.code = code;
    }
}
