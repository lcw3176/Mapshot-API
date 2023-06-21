package com.mapshot.api.common.validation;

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
