package com.mapshot.api.slack.client;


import com.mapshot.api.common.client.CommonClient;
import com.mapshot.api.slack.model.MessageResponse;
import com.mapshot.api.slack.util.SlackMessageFormatter;
import java.util.Arrays;
import org.springframework.stereotype.Component;

@Component
public class SlackClient extends CommonClient {

    private final String slackUrl = System.getenv("SLACK_URL");


    public void sendMessage(Exception e) {
        MessageResponse errorMessage = MessageResponse.builder()
                .title(e.getClass().getName())
                .message(makeTransmissible(e))
                .build();

        sendSlackMessage(errorMessage);
    }

    private void sendSlackMessage(MessageResponse exception) {
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