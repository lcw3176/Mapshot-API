package com.mapshot.api.auth.enums;

import com.mapshot.api.auth.validation.AdminValidation;
import com.mapshot.api.auth.validation.ServerValidation;
import com.mapshot.api.auth.validation.Validation;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Accessible {

    FRIENDLY_SERVER(ServerValidation.class),
    ADMIN(AdminValidation.class),
    EVERYONE(null),
    ;


    private final Class<? extends Validation> validationClass;
}
