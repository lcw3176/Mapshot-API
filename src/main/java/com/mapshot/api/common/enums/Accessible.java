package com.mapshot.api.common.enums;

import com.mapshot.api.common.validation.AdminValidation;
import com.mapshot.api.common.validation.ServerValidation;
import com.mapshot.api.common.validation.Validation;
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
