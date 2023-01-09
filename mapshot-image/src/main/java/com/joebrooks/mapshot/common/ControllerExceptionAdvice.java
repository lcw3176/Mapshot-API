package com.joebrooks.mapshot.common;

import com.joebrooks.mapshot.client.SlackClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ControllerExceptionAdvice {

    private final SlackClient slackClient;

    @ExceptionHandler(Exception.class)
    public void exceptionHandler(Exception exception) {
        log.error(exception.getMessage(), exception);
        slackClient.sendMessage(exception);
    }

    @MessageExceptionHandler(Exception.class)
    public void messageExceptionHandler(Exception exception) {
        log.error(exception.getMessage(), exception);
        slackClient.sendMessage(exception);
    }

}