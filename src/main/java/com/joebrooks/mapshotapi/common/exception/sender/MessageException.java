package com.joebrooks.mapshotapi.common.exception.sender;

public class MessageException extends RuntimeException {

    public MessageException(String msg) {
        super(msg);
    }

    public MessageException(String msg, Throwable e) {
        super(msg, e);
    }

}
