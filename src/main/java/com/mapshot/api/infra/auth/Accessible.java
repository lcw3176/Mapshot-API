package com.mapshot.api.infra.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Accessible {

    ADMIN(AdminValidator.class),
//    EVERYONE(null),
    ;


    private final Class<? extends Validator> validationClass;
}
