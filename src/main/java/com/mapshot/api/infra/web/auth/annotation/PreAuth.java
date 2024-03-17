package com.mapshot.api.infra.web.auth.annotation;

import com.mapshot.api.infra.web.auth.enums.Accessible;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PreAuth {

    Accessible[] value();

}
