package com.mapshot.api.infra.auth;


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
public class AdminValidator implements Validator {


    private final TokenProvider tokenProvider;

    @Value("${jwt.admin.secret}")
    private String JWT_SECRET;


    @Value("${jwt.admin.header}")
    private String ADMIN_HEADER_NAME;


    @Override
    public void isValidOrThrowException(HttpServletRequest request) {

        if (request.getCookies() == null) {
            throw new ApiException(ErrorCode.NO_AUTH_TOKEN);
        }

        Cookie cookie = Arrays.stream(request.getCookies())
                .filter(i -> i.getName().equals(ADMIN_HEADER_NAME))
                .findAny()
                .orElseThrow(() -> new ApiException(ErrorCode.NO_AUTH_TOKEN));

        String token = cookie.getValue();

        boolean isValid = tokenProvider.isValid(JWT_SECRET, token);

        if(!isValid){
            throw new ApiException(ErrorCode.NOT_VALID_TOKEN);
        }
    }

}