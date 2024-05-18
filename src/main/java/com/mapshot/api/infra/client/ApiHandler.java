package com.mapshot.api.infra.client;


import com.mapshot.api.infra.exception.ApiException;
import com.mapshot.api.infra.exception.status.ErrorCode;

import java.util.function.Supplier;

public class ApiHandler {

    public static <T> T handle(Supplier<T> supplier) {
        try {
            return supplier.get();
        } catch (RuntimeException e) {
            throw new ApiException(ErrorCode.EXTERNAL_API_FAILED, e);
        }
    }

}
