package com.joebrooks.mapshot.client;

import com.joebrooks.mapshot.model.MessageResponse;
import com.joebrooks.mapshot.util.SlackMessageFormatter;
import java.util.Arrays;

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
