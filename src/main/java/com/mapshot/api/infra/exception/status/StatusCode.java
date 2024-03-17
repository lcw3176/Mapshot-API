package com.mapshot.api.infra.exception.status;

import org.springframework.http.HttpStatus;

public interface StatusCode {
    HttpStatus getHttpStatus();

    String getMessage();
}
