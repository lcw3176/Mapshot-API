package com.mapshot.api.auth.validation;

import com.mapshot.api.auth.validation.token.AdminToken;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;


@Component
@RequiredArgsConstructor
public class AdminValidation implements Validation {

    private final AdminToken adminToken;

    @Value("${jwt.admin.header}")
    private String ADMIN_HEADER_NAME;


    @Override
    public void checkValidation(HttpServletRequest request) {
        String token = request.getHeader(ADMIN_HEADER_NAME);
        adminToken.isValid(token);
    }

    @Override
    public Object getAuth() {
        return adminToken.makeToken();
    }

    @Override
    public MultiValueMap<String, String> getHeader() {
        return adminToken.getTokenHeader();
    }
}
