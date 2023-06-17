package com.mapshot.api.common.annotation;

import com.mapshot.api.common.enums.AuthType;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PreAuth {

    AuthType[] value();

}
