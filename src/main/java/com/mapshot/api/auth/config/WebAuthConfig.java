package com.mapshot.api.auth.config;

import com.mapshot.api.auth.interceptor.AuthInterceptor;
import com.mapshot.api.auth.validation.token.AdminToken;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebAuthConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;
    private final AdminToken adminToken;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(authInterceptor)
                .order(1)
                .excludePathPatterns("/css/**")
                .excludePathPatterns("/js/**")
                .excludePathPatterns("/images/**")
                .excludePathPatterns("/error/**")
                .excludePathPatterns("/download/**")
                .excludePathPatterns("/favicon.ico")
                .addPathPatterns("/**");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/admin/**")
                .exposedHeaders(adminToken.getHeaderName())
                .allowCredentials(true);

    }

}
