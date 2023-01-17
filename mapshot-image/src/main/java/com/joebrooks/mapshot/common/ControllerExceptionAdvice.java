package com.joebrooks.mapshot.common;

import com.joebrooks.mapshot.client.SlackClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class ControllerExceptionAdvice {

    private final SlackClient slackClient = new SlackClient();


    @ExceptionHandler(Exception.class)
    public void exceptionHandler(Exception exception) {
        log.error(exception.getMessage(), exception);
        slackClient.sendMessage(exception);
    }


}