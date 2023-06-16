package com.mapshot.api.common.interceptor;

import com.mapshot.api.common.exception.ApiException;
import com.mapshot.api.common.exception.status.ErrorCode;
import com.mapshot.api.common.token.JwtProvider;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        String token = request.getHeader(JwtProvider.HEADER_NAME);
        System.out.println("토큰: " + token);
        System.out.println(JwtProvider.isValid(token));
        if (!JwtProvider.isValid(token)) {
            throw new ApiException(ErrorCode.NO_AUTH_TOKEN);
        }

        return true;
    }
}
