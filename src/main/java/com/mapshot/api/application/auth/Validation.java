package com.mapshot.api.application.auth;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;

public interface Validation {

    void checkValidation(HttpServletRequest request);

    String makeToken();

    HttpHeaders makeHeader();
}
