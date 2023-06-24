package com.mapshot.api.common.exception;

import com.mapshot.api.common.exception.status.StatusCode;
import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {

    private StatusCode code;

    public ApiException(String msg) {
        super(msg);
    }

    public ApiException(StatusCode code) {
        super(code.getMessage());
        this.code = code;
    }

    public ApiException(String msg, Throwable e) {
        super(msg, e);
    }

}
