package com.mapshot.api.application.auth.annotation;

import com.mapshot.api.application.auth.enums.Accessible;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PreAuth {

    Accessible[] value();

}
