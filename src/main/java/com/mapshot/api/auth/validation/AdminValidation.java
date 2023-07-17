package com.mapshot.api.auth.validation;

import com.mapshot.api.auth.validation.token.AdminToken;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class AdminValidation implements Validation {

    private final AdminToken adminToken;

    @Override
    public void checkValidation(HttpServletRequest request) {
        String token = request.getHeader(adminToken.getHeaderName());
        adminToken.isValid(token);
    }
}
