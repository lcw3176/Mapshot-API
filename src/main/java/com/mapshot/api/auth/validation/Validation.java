package com.mapshot.api.auth.validation;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.MultiValueMap;

public interface Validation {

    void checkValidation(HttpServletRequest request);

    Object getAuth();

    MultiValueMap<String, String> getHeader();
}
