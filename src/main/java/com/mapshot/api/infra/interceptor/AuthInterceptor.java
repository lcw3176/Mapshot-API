package com.mapshot.api.infra.interceptor;

import com.mapshot.api.application.auth.Validation;
import com.mapshot.api.application.auth.PreAuth;
import com.mapshot.api.application.auth.Accessible;
import com.mapshot.api.infra.exception.ApiException;
import com.mapshot.api.infra.exception.status.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final ApplicationContext applicationContext;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {

        if (isPreflightRequest(request)) {
            return true;
        }

        HandlerMethod method = null;

        try {
            method = (HandlerMethod) handler;
        } catch (Exception e) {
            throw new ApiException(ErrorCode.HANDLER_NOT_FOUND);
        }

        PreAuth preAuth = method.getMethodAnnotation(PreAuth.class);

        if (preAuth == null) {
            return true;
        }

        Accessible[] accessible = preAuth.value();

        for (Accessible type : accessible) {
            Validation validation = applicationContext.getBean(type.getValidationClass());

            // fixme
            // 여기도 나중에 좀 어떻게
            if (!validation.checkValidation(request)) {
                response.setStatus(HttpStatus.FOUND.value());
                return false;
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
