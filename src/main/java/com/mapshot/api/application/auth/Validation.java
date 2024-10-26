package com.mapshot.api.application.auth;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;

// fixme 얘좀 없애자
public interface Validation {

    void checkValidation(HttpServletRequest request);

    String makeToken();

    HttpHeaders makeHeader();

    Cookie makeCookie();

    boolean isAuthUser(HttpServletRequest request);
}
