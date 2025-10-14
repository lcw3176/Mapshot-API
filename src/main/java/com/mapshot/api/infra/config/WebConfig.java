package com.mapshot.api.infra.config;

import com.mapshot.api.infra.interceptor.AuthInterceptor;
import com.mapshot.api.infra.interceptor.RateLimitInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;
    private final RateLimitInterceptor rateLimitInterceptor;
    @Value("${jwt.admin.header}")
    public String ADMIN_HEADER_NAME;

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

        registry.addInterceptor(rateLimitInterceptor)
                .addPathPatterns("/map/**");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/admin/**")
                .exposedHeaders(ADMIN_HEADER_NAME)
                .allowedOrigins("https://www.kmapshot.com", "https://kmapshot.com", "https://dev.kmapshot.com", "https://admin.kmapshot.com")
                .allowedMethods(HttpMethod.GET.name(), HttpMethod.POST.name(), HttpMethod.OPTIONS.name())
                .allowedHeaders("*")
                .allowCredentials(true);

        registry.addMapping("/**")
                .allowedOrigins("https://www.kmapshot.com", "https://kmapshot.com", "https://dev.kmapshot.com", "https://admin.kmapshot.com")
                .allowedMethods(HttpMethod.GET.name(), HttpMethod.POST.name(), HttpMethod.OPTIONS.name())
                .allowedHeaders("*")
                .allowCredentials(true);
    }

}
