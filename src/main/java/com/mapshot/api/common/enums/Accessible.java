package com.mapshot.api.common.enums;

import com.mapshot.api.common.token.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Function;

@Getter
@AllArgsConstructor
public enum Accessible {

    FRIENDLY_SERVER(JwtUtil.HEADER_NAME, JwtUtil::isValid),
    ADMIN(JwtUtil.ADMIN_HEADER_NAME, JwtUtil::isValidAdmin),
    EVERYONE("", (str) -> true),
    ;

    private final String requiredToken;
    private final Function<String, Boolean> validationFunction;

}
