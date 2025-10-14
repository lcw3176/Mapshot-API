package com.mapshot.api.infra.client.slack;


import com.mapshot.api.infra.client.ApiHandler;
import com.mapshot.api.infra.client.slack.model.SlackMessage;
import com.mapshot.api.infra.client.slack.util.SlackMessageFormatter;
import com.mapshot.api.infra.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class SlackClient {


    private final RestClient restClient;
    @Value("${client.slack.url}")
    private String slackUrl;

    public void sendMessage(String title, String message) {
        SlackMessage slackMessage = SlackMessage.builder()
                .title(title)
                .message(makeTransmissible(message))
                .build();

        sendSlackMessage(slackMessage);
    }

    public void sendMessage(ApiException e) {
        SlackMessage slackMessage = SlackMessage.builder()
                .title(e.getCode().getMessage())
                .message(makeTransmissible(e))
                .build();

        sendSlackMessage(slackMessage);
    }

    public void sendMessage(Throwable e) {
        SlackMessage slackMessage = SlackMessage.builder()
                .title(e.getClass().getName())
                .message(makeTransmissible(e))
                .build();

        sendSlackMessage(slackMessage);
    }

    private String makeTransmissible(Throwable e) {
        String stackTrace = Arrays.toString(e.getStackTrace());

        return makeTransmissible(stackTrace);
    }

    private String makeTransmissible(String stackTrace) {
        int len = Math.min(stackTrace.length(), 1700);

        return stackTrace.substring(0, len);
    }

    private void sendSlackMessage(SlackMessage exception) {
        String message = SlackMessageFormatter.makeExceptionMessage(exception);

        ApiHandler.handle(() -> restClient.post()
                .uri(slackUrl)
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .body(message)
                .retrieve()
                .onStatus(HttpStatusCode::isError, ((request, response) -> {
                    throw new RuntimeException("status: " + response.getStatusCode() + " body: " + response.getBody());
                }))
                .body(String.class)
        );
    }


}
