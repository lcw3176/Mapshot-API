package com.mapshot.api.infra.interceptor;

import com.mapshot.api.infra.exception.ApiException;
import com.mapshot.api.infra.exception.status.ErrorCode;
import io.github.bucket4j.Bucket;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class RateLimitInterceptor implements HandlerInterceptor {

    private final Bucket bucket;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (bucket.tryConsume(1)) {
            return true;
        }

        throw new ApiException(ErrorCode.RATE_LIMIT_ACTIVATED);
    }
}