package com.mapshot.api;


import com.mapshot.api.infra.client.slack.SlackClient;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;


public class SlackMockExtension implements BeforeEachCallback {

    @MockBean
    private SlackClient slackClient;

    @Override
    public void beforeEach(ExtensionContext context) {
        slackClient = Mockito.mock(SlackClient.class);
        Mockito.doNothing().when(slackClient).sendMessage(Mockito.any());
    }
}
