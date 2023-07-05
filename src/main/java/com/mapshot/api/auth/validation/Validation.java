package com.mapshot.api.auth.validation;

import javax.servlet.http.HttpServletRequest;

public interface Validation {

    void checkValidation(HttpServletRequest request);
}
