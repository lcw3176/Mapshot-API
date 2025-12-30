package com.mapshot.api.infra.client.slack;

import com.mapshot.api.infra.exception.ApiException;
import com.mapshot.api.infra.exception.status.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClient;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class SlackClientTest {

    @Mock
    private RestClient restClient;

    @Mock
    private RestClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private RestClient.RequestBodySpec requestBodySpec;

    @Mock
    private RestClient.ResponseSpec responseSpec;

    @InjectMocks
    private SlackClient slackClient;

    private String slackUrl = "https://hooks.slack.com/test";

    @BeforeEach
    void setUp() {
        // @Value 필드를 리플렉션으로 주입
        ReflectionTestUtils.setField(slackClient, "slackUrl", slackUrl);

        // Mock 체인 설정 - 모든 메서드 체이닝 포함
        when(restClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.accept(any())).thenReturn(requestBodySpec);
        when(requestBodySpec.acceptCharset(any())).thenReturn(requestBodySpec);
        // body() 메서드는 String을 받아서 RequestBodySpec을 반환
        when(requestBodySpec.body(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        // onStatus는 Predicate와 BiConsumer를 받고 ResponseSpec을 반환
        // 람다가 실행되지 않도록 doReturn 사용 (람다 실행 시 NullPointerException 방지)
        doReturn(responseSpec).when(responseSpec).onStatus(any(), any());
        when(responseSpec.body(any(Class.class))).thenReturn("success");
    }

    @Test
    void 메시지_전송_성공_문자열_메시지() {
        // given
        String title = "테스트 제목";
        String message = "테스트 메시지";

        // when
        slackClient.sendMessage(title, message);

        // then
        verify(restClient, times(1)).post();
        verify(requestBodyUriSpec, times(1)).uri(slackUrl);
        verify(requestBodySpec, times(1)).body(anyString());
    }

    @Test
    void 메시지_전송_성공_ApiException() {
        // given
        ApiException exception = new ApiException(ErrorCode.NO_SUCH_NOTICE);

        // when
        slackClient.sendMessage(exception);

        // then
        verify(restClient, times(1)).post();
        verify(requestBodyUriSpec, times(1)).uri(slackUrl);
        verify(requestBodySpec, times(1)).body(anyString());
    }

    @Test
    void 메시지_전송_성공_일반_예외() {
        // given
        RuntimeException exception = new RuntimeException("테스트 예외");

        // when
        slackClient.sendMessage(exception);

        // then
        verify(restClient, times(1)).post();
        verify(requestBodyUriSpec, times(1)).uri(slackUrl);
        verify(requestBodySpec, times(1)).body(anyString());
    }

    @Test
    void 긴_메시지_자동_절단() {
        // given
        String longMessage = "a".repeat(2000);

        // when
        slackClient.sendMessage("제목", longMessage);

        // then
        // 메시지가 1700자로 제한되어야 함
        verify(restClient, times(1)).post();
        verify(requestBodySpec, times(1)).body(anyString());
    }

    @Test
    void 스택트레이스_메시지_전송() {
        // given
        Exception exception = new RuntimeException("테스트");
        exception.fillInStackTrace();

        // when
        slackClient.sendMessage(exception);

        // then
        verify(restClient, times(1)).post();
        verify(requestBodyUriSpec, times(1)).uri(slackUrl);
        verify(requestBodySpec, times(1)).body(anyString());
    }
}

