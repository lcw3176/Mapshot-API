package com.mapshot.api.common.config;

import javax.servlet.Filter;

import com.mapshot.api.admin.filter.AdminAuthFilter;
import com.mapshot.api.image.filter.ImageAuthFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
public class FilterConfig {


    @Bean
    public FilterRegistrationBean<Filter> imageAuthFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new ImageAuthFilter());
        filterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        filterRegistrationBean.addUrlPatterns("/image/*");

        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean<Filter> adminAuthFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new AdminAuthFilter());
        filterRegistrationBean.addUrlPatterns("/admin/*");

        return filterRegistrationBean;
    }

}