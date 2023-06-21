package com.mapshot.api.common.validation;

import com.mapshot.api.common.validation.token.ImageToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
@RequiredArgsConstructor
public class ServerValidation implements Validation {

    private final ImageToken imageToken;

    @Override
    public void checkValidation(HttpServletRequest request) {
        String token = request.getHeader(imageToken.getHeaderName());
        imageToken.isValid(token);
    }
}
