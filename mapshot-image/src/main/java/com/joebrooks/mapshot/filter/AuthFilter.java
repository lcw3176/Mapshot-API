package com.joebrooks.mapshot.filter;


import com.joebrooks.mapshot.auth.JwtTokenProvider;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String token = httpRequest.getHeader(JwtTokenProvider.HEADER_NAME);

        if (!JwtTokenProvider.isValid(token)) {

            httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        chain.doFilter(request, response);
    }

}