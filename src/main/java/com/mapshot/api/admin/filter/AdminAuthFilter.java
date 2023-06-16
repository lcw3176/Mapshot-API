package com.mapshot.api.admin.filter;


import com.mapshot.api.common.token.JwtProvider;
import org.springframework.util.PatternMatchUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AdminAuthFilter implements Filter {

    private static final String[] whitelist = {"/admin/login"};

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String token = httpRequest.getHeader(JwtProvider.HEADER_NAME);

        if (isNotWhitelistPath(httpRequest.getRequestURI()) && !JwtProvider.isValid(token)) {
            httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean isNotWhitelistPath(String requestURI) {
        return !PatternMatchUtils.simpleMatch(whitelist, requestURI);
    }


}
