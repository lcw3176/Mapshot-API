package com.mapshot.api.common.config;

import com.mapshot.api.common.interceptor.AuthInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(authInterceptor)
                .order(1)
                .excludePathPatterns("/image/queue")
                .excludePathPatterns("/image/storage/*")
                .excludePathPatterns("/image/template/*")

                .excludePathPatterns("/notice/detail/*")
                .excludePathPatterns("/notice/summary/*")

                .excludePathPatterns("/admin/login")

                .addPathPatterns("/**");
    }

}
