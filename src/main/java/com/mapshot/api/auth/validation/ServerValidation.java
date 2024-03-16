package com.mapshot.api.auth.validation;

import com.mapshot.api.auth.validation.token.ImageToken;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;


@Component
@RequiredArgsConstructor
public class ServerValidation implements Validation {

    private final ImageToken imageToken;

    @Override
    public void checkValidation(HttpServletRequest request) {
        String token = request.getHeader(imageToken.getHeaderName());
        imageToken.isValid(token);
    }

    @Override
    public Object getAuth() {
        return null;
    }

    @Override
    public MultiValueMap<String, String> getHeader() {
        return null;
    }
}
