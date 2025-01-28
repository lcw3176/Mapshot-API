package com.mapshot.api.application.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Accessible {

    ADMIN(AdminValidation.class),
//    EVERYONE(null),
    ;


    private final Class<? extends Validation> validationClass;
}
