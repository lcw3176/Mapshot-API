package com.mapshot.api.infra.auth.annotation;

import com.mapshot.api.infra.auth.enums.Accessible;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PreAuth {

    Accessible[] value();

}
