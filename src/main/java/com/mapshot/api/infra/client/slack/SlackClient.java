package com.mapshot.api.infra.client.slack;


import com.mapshot.api.infra.client.CommonClient;
import com.mapshot.api.infra.client.slack.model.SlackMessage;
import com.mapshot.api.infra.client.slack.util.SlackMessageFormatter;
import com.mapshot.api.infra.exception.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class SlackClient extends CommonClient {

    @Value("${slack.url}")
    private String slackUrl;


    public void sendMessage(ApiException e) {
        SlackMessage slackMessage = SlackMessage.builder()
                .title(e.getCode().getMessage())
                .message(makeTransmissible(e))
                .build();

        sendSlackMessage(slackMessage);
    }

    public void sendMessage(Exception e) {
        SlackMessage slackMessage = SlackMessage.builder()
                .title(e.getClass().getName())
                .message(makeTransmissible(e))
                .build();

        sendSlackMessage(slackMessage);
    }

    private void sendSlackMessage(SlackMessage exception) {
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
