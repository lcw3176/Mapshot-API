package com.mapshot.api.common.validation;

import com.mapshot.api.common.validation.token.AdminToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

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