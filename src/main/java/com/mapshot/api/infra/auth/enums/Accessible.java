package com.mapshot.api.infra.auth.enums;

import com.mapshot.api.infra.auth.AdminValidation;
import com.mapshot.api.infra.auth.ServerValidation;
import com.mapshot.api.infra.auth.Validation;
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
