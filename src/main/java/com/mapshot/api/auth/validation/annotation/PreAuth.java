package com.mapshot.api.auth.validation.annotation;

import com.mapshot.api.auth.validation.Accessible;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PreAuth {

    Accessible[] value();

}
