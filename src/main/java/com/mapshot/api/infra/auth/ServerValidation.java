package com.mapshot.api.infra.auth;

import com.mapshot.api.infra.auth.token.TokenProcessor;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;


@Component
@RequiredArgsConstructor
public class ServerValidation implements Validation {

    private final TokenProcessor tokenProcessor;

    @Value("${jwt.image.secret}")
    private String JWT_SECRET;

    @Value("${jwt.image.second}")
    private int DEFAULT_SECONDS;

    @Value("${jwt.image.header}")
    private String SERVER_HEADER_NAME;


    @Override
    public void checkValidation(HttpServletRequest request) {
        String token = request.getHeader(SERVER_HEADER_NAME);

        tokenProcessor.isValid(JWT_SECRET, token);
    }

    @Override
    public String makeToken() {
        String token = tokenProcessor.makeToken(DEFAULT_SECONDS, JWT_SECRET);

        return token;
    }

    @Override
    public HttpHeaders makeHeader() {
        String token = tokenProcessor.makeToken(DEFAULT_SECONDS, JWT_SECRET);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add(SERVER_HEADER_NAME, token);

        return HttpHeaders.readOnlyHttpHeaders(map);
    }
}
