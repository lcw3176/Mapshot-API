package com.mapshot.api.common.interceptor;

import com.mapshot.api.common.annotation.PreAuth;
import com.mapshot.api.common.enums.AuthType;
import com.mapshot.api.common.exception.ApiException;
import com.mapshot.api.common.exception.status.ErrorCode;
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

        HandlerMethod method = (HandlerMethod) handler;
        PreAuth preAuth = method.getMethodAnnotation(PreAuth.class);


        if (preAuth == null) {
            throw new ApiException(ErrorCode.NO_AUTH_TOKEN);
        }

        AuthType[] authTypes = preAuth.value();

        for (AuthType type : authTypes) {

            String token = request.getHeader(type.getRequiredToken());

            if (StringUtil.isNullOrEmpty(token)) {
                throw new ApiException(ErrorCode.NO_AUTH_TOKEN);
            }

            boolean isValid = type.getValidationFunction().apply(token);

            if (!isValid) {
                throw new ApiException(ErrorCode.NOT_VALID_TOKEN);
            }
        }

        return true;
    }


}
