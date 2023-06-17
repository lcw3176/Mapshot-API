package com.mapshot.api.common.interceptor;

import com.mapshot.api.common.annotation.PreAuth;
import com.mapshot.api.common.enums.AuthType;
import com.mapshot.api.common.exception.ApiException;
import com.mapshot.api.common.exception.status.ErrorCode;
import com.mapshot.api.common.token.JwtUtil;
import io.netty.util.internal.StringUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        String token = request.getHeader(JwtUtil.HEADER_NAME);

        if (StringUtil.isNullOrEmpty(token)) {
            throw new ApiException(ErrorCode.NO_AUTH_TOKEN);
        }

        HandlerMethod method = (HandlerMethod) handler;
        PreAuth preAuth = method.getMethodAnnotation(PreAuth.class);

        if (preAuth == null) {
            throw new ApiException(ErrorCode.NO_AUTH_TOKEN);
        }

        AuthType[] authTypes = preAuth.value();

        for (AuthType type : authTypes) {
            boolean isValid = type.getValidationFunction().apply(token);

            if (!isValid) {
                throw new ApiException(ErrorCode.NO_AUTH_TOKEN);
            }
        }

        return true;
    }


}
