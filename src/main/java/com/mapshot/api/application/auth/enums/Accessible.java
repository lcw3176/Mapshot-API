package com.mapshot.api.application.auth.enums;

import com.mapshot.api.application.auth.AdminValidation;
import com.mapshot.api.application.auth.Validation;
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
