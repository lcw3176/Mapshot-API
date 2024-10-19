package com.mapshot.api.application.auth;

import com.mapshot.api.application.auth.token.TokenProcessor;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
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
    public String makeToken() {
        return tokenProcessor.makeToken(DEFAULT_SECONDS, JWT_SECRET);
    }

    @Override
    public HttpHeaders makeHeader() {
        String token = tokenProcessor.makeToken(DEFAULT_SECONDS, JWT_SECRET);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add(ADMIN_HEADER_NAME, token);

        return HttpHeaders.readOnlyHttpHeaders(map);
    }

    @Override
    public Cookie makeCookie() {
        Cookie cookie = new Cookie(ADMIN_HEADER_NAME, tokenProcessor.makeToken(DEFAULT_SECONDS, JWT_SECRET));
        cookie.setHttpOnly(true);
        cookie.setMaxAge(60 * 60);

        return cookie;
    }
}
