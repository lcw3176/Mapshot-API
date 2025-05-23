package com.mapshot.api.application.auth;

import com.mapshot.api.infra.exception.ApiException;
import com.mapshot.api.infra.exception.status.ErrorCode;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;


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
    public boolean checkValidation(HttpServletRequest request) {

        if (request.getCookies() == null) {
            throw new ApiException(ErrorCode.NO_AUTH_TOKEN);
        }

        Cookie cookie = Arrays.stream(request.getCookies())
                .filter(i -> i.getName().equals(ADMIN_HEADER_NAME))
                .findAny()
                .orElseThrow(() -> new ApiException(ErrorCode.NO_AUTH_TOKEN));

        String token = cookie.getValue();

        return tokenProcessor.isValid(JWT_SECRET, token);
    }

    @Override
    public String makeToken() {
        return tokenProcessor.makeToken(DEFAULT_SECONDS, JWT_SECRET);
    }


    @Override
    public Cookie makeCookie() {
        Cookie cookie = new Cookie(ADMIN_HEADER_NAME, tokenProcessor.makeToken(DEFAULT_SECONDS, JWT_SECRET));
        cookie.setMaxAge(60 * 60);
        cookie.setSecure(true);
        cookie.setDomain("kmapshot.com");
        cookie.setPath("/");

        return cookie;
    }
}
