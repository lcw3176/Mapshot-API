package com.mapshot.api.auth.validation;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;


@Component
@RequiredArgsConstructor
public class AdminValidation implements Validation {


    private final TokenProcessor tokenProcessor;

    @Value("${jwt.admin.secret}")
    private String JWT_SECRET;

    @Value("${jwt.admin.second}")
    private int DEFAULT_SECONDS;

    @Value("${jwt.admin.header}")
    private String ADMIN_HEADER_NAME;


    @Override
    public void checkValidation(HttpServletRequest request) {
        String token = request.getHeader(ADMIN_HEADER_NAME);
        tokenProcessor.isValid(JWT_SECRET, token);
    }

    @Override
    public String getToken() {
        return tokenProcessor.makeToken(DEFAULT_SECONDS, JWT_SECRET);
    }

    @Override
    public MultiValueMap<String, String> getHeader() {
        String token = tokenProcessor.makeToken(DEFAULT_SECONDS, JWT_SECRET);

        return tokenProcessor.getTokenHeader(ADMIN_HEADER_NAME, token);
    }
}
