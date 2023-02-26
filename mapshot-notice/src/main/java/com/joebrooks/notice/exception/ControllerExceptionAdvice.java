package com.joebrooks.notice.exception;


import com.joebrooks.common.client.SlackClient;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
@Slf4j
public class ControllerExceptionAdvice {

    private final SlackClient slackClient = new SlackClient();

    @ExceptionHandler({ConstraintViolationException.class, IllegalStateException.class,
            MethodArgumentTypeMismatchException.class, IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void violationExceptionHandler(Exception e) {
        log.info(e.getMessage(), e);

    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void exceptionHandler(Exception exception) {
        log.error(exception.getMessage(), exception);
        slackClient.sendMessage(exception);

    }

}