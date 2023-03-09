package com.mapshot.api.slack.model;

public class MessageException extends RuntimeException {

    public MessageException(String msg) {
        super(msg);
    }

    public MessageException(String msg, Throwable e) {
        super(msg, e);
    }

}
