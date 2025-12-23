package com.mapshot.api.infra.auth;

import jakarta.servlet.http.HttpServletRequest;

public interface Validator {

    void isValidOrThrowException(HttpServletRequest request);

}
