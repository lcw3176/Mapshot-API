package com.mapshot.api.auth.validation;


import jakarta.servlet.http.HttpServletRequest;

public interface Validation {

    void checkValidation(HttpServletRequest request);
}
