package com.mapshot.api.common.interceptor;

import com.mapshot.api.common.annotation.PreAuth;
import com.mapshot.api.common.enums.Accessible;
import com.mapshot.api.common.exception.ApiException;
import com.mapshot.api.common.exception.status.ErrorCode;
import io.netty.util.internal.StringUtil;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        if (isPreflightRequest(request)) {
            return true;
        }

        HandlerMethod method = (HandlerMethod) handler;
        PreAuth preAuth = method.getMethodAnnotation(PreAuth.class);


        if (preAuth == null) {
            throw new ApiException(ErrorCode.NO_AUTH_TOKEN);
        }

        Accessible[] accessibles = preAuth.value();

        for (Accessible type : accessibles) {

            if (type == Accessible.EVERYONE) {
                continue;
            }

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


    private boolean isPreflightRequest(HttpServletRequest request) {
        return isOptions(request) && hasHeaders(request) && hasMethod(request) && hasOrigin(request);
    }

    private boolean isOptions(HttpServletRequest request) {
        return request.getMethod().equalsIgnoreCase(HttpMethod.OPTIONS.toString());
    }

    private boolean hasHeaders(HttpServletRequest request) {
        return Objects.nonNull(request.getHeader("Access-Control-Request-Headers"));
    }

    private boolean hasMethod(HttpServletRequest request) {
        return Objects.nonNull(request.getHeader("Access-Control-Request-Method"));
    }

    private boolean hasOrigin(HttpServletRequest request) {
        return Objects.nonNull(request.getHeader("Origin"));
    }

}
