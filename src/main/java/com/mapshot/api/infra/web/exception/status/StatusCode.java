package com.mapshot.api.infra.web.exception.status;

import org.springframework.http.HttpStatus;

public interface StatusCode {
    HttpStatus getHttpStatus();

    String getMessage();
}
