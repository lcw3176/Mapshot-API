package com.mapshot.api.common.exception.status;

import org.springframework.http.HttpStatus;

public interface StatusCode {
    HttpStatus getHttpStatus();

    String getMessage();
}
