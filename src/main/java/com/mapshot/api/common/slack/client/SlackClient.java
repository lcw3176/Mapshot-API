package com.mapshot.api.common.slack.client;


import com.mapshot.api.common.client.CommonClient;
import com.mapshot.api.common.exception.ApiException;
import com.mapshot.api.common.slack.model.ErrorMessage;
import com.mapshot.api.common.slack.util.SlackMessageFormatter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class SlackClient extends CommonClient {

    @Value("${slack.url}")
    private String slackUrl;


    public void sendMessage(ApiException e) {
        ErrorMessage errorMessage = ErrorMessage.builder()
                .title(e.getCode().getMessage())
                .message(makeTransmissible(e))
                .build();

        sendSlackMessage(errorMessage);
    }

    public void sendMessage(Exception e) {
        ErrorMessage errorMessage = ErrorMessage.builder()
                .title(e.getClass().getName())
                .message(makeTransmissible(e))
                .build();

        sendSlackMessage(errorMessage);
    }

    private void sendSlackMessage(ErrorMessage exception) {
        int timeoutMillis = 3000;

        String message = SlackMessageFormatter.makeExceptionMessage(exception);
        post(slackUrl, timeoutMillis, message, String.class);
    }

    private String makeTransmissible(Exception e) {
        String stackTrace = Arrays.toString(e.getStackTrace());
        int len = Math.min(stackTrace.length(), 1700);

        return stackTrace.substring(0, len);
    }
}
