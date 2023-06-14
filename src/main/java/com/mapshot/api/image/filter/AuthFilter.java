package com.mapshot.api.image.filter;


import com.mapshot.api.common.token.JwtTokenProvider;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.util.PatternMatchUtils;

public class AuthFilter implements Filter {

    private static final String[] whitelist = {"/image/queue", "/image/storage/*", "/image/template/*"};

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String token = httpRequest.getHeader(JwtTokenProvider.HEADER_NAME);

        if (isNotWhitelistPath(httpRequest.getRequestURI()) && !JwtTokenProvider.isValid(token)) {
            httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean isNotWhitelistPath(String requestURI) {
        return !PatternMatchUtils.simpleMatch(whitelist, requestURI);
    }


}
