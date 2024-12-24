package com.mapshot.api.application.auth;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

// fixme 얘좀 없애자
public interface Validation {

    boolean checkValidation(HttpServletRequest request);

    String makeToken();

    Cookie makeCookie();

}
