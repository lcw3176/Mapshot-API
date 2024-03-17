package com.mapshot.api.infra.web.auth.enums;

import com.mapshot.api.infra.web.auth.AdminValidation;
import com.mapshot.api.infra.web.auth.ServerValidation;
import com.mapshot.api.infra.web.auth.Validation;
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
