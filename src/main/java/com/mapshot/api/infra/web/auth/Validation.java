package com.mapshot.api.infra.web.auth;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.MultiValueMap;

public interface Validation {

    void checkValidation(HttpServletRequest request);

    String getToken();

    MultiValueMap<String, String> getHeader();
}
