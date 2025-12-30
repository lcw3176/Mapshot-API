package com.mapshot.api.infra.auth;


import com.mapshot.api.infra.exception.ApiException;
import com.mapshot.api.infra.exception.status.ErrorCode;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class AdminValidator implements Validator {

    @Value("${jwt.admin.header}")
    private String ADMIN_SESSION;


    @Override
    public void isValidOrThrowException(HttpServletRequest request) {

        if (request.getSession() == null) {
            throw new ApiException(ErrorCode.NO_AUTH_TOKEN);
        }

        Object isAdmin = request.getSession().getAttribute(ADMIN_SESSION);

        if(isAdmin == null || !((Boolean) isAdmin)){
            throw new ApiException(ErrorCode.NO_AUTH_TOKEN);
        }
    }


}