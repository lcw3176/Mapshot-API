package com.joebrooks.mapshotapi.common.exception.sender.slack;

import com.joebrooks.mapshotapi.common.exception.sender.MessageClient;
import com.joebrooks.mapshotapi.common.exception.sender.MessageResponse;
import java.util.Arrays;
import org.springframework.stereotype.Component;

@Component
public class SlackClient extends MessageClient {

    private final String slackUrl = System.getenv("SLACK_URL");

    public void sendMessage(Exception e) {
        MessageResponse errorMessage = MessageResponse.builder()
                .title(e.getClass().getName())
                .message(makeTransmissible(e))
                .build();

        sendSlackMessage(errorMessage);
    }

    private void sendSlackMessage(MessageResponse exception) {
        String message = SlackMessageFormatter.makeExceptionMessage(exception);
        post(slackUrl, message);
    }

    private String makeTransmissible(Exception e) {
        String stackTrace = Arrays.toString(e.getStackTrace());
        int len = Math.min(stackTrace.length(), 1700);

        return stackTrace.substring(0, len);
    }
}
