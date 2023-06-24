package com.mapshot.api.common.validation;

import javax.servlet.http.HttpServletRequest;

public interface Validation {

    void checkValidation(HttpServletRequest request);
}
