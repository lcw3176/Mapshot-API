package com.mapshot.api.auth.annotation;

import com.mapshot.api.auth.enums.Accessible;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PreAuth {

    Accessible[] value();

}
