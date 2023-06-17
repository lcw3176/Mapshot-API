package com.mapshot.api.common.enums;

import com.mapshot.api.common.token.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Function;

@Getter
@AllArgsConstructor
public enum AuthType {

    IMAGE(JwtUtil::isValid),
    ADMIN(JwtUtil::isValidAdmin),
    ;


    private final Function<String, Boolean> validationFunction;

}
