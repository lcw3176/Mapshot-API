package com.mapshot.api.auth.validation;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;


@Component
@RequiredArgsConstructor
public class ServerValidation implements Validation {

    private final TokenProcessor tokenProcessor;

    @Value("${jwt.image.secret}")
    private String JWT_SECRET;

    @Value("${jwt.image.second}")
    private int DEFAULT_SECONDS;

    @Value("${jwt.image.header}")
    private String SERVER_HEADER_NAME;


    @Override
    public void checkValidation(HttpServletRequest request) {
        String token = request.getHeader(SERVER_HEADER_NAME);
        tokenProcessor.isValid(JWT_SECRET, token);
    }

    @Override
    public String getToken() {
        return tokenProcessor.makeToken(DEFAULT_SECONDS, JWT_SECRET);
    }

    @Override
    public MultiValueMap<String, String> getHeader() {
        String token = tokenProcessor.makeToken(DEFAULT_SECONDS, JWT_SECRET);

        return tokenProcessor.getTokenHeader(SERVER_HEADER_NAME, token);
    }
}
