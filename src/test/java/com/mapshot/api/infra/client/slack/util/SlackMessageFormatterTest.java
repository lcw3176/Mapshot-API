package com.mapshot.api.infra.client.slack.util;

import com.mapshot.api.infra.client.slack.model.SlackMessage;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SlackMessageFormatterTest {

    @Test
    void 예외_메시지_포맷팅_성공() throws Exception {
        // given
        SlackMessage message = SlackMessage.builder()
                .title("테스트 예외")
                .message("예외 메시지 내용")
                .build();

        // when
        String formatted = SlackMessageFormatter.makeExceptionMessage(message);

        // then
        assertThat(formatted).isNotNull();
        assertThat(formatted).isNotEmpty();

        // JSON 파싱 가능한지 확인
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(formatted);
        assertThat(json).isNotNull();
        assertThat(json.containsKey("blocks")).isTrue();
    }

    @Test
    void 예외_메시지_포맷팅_빈_제목() throws Exception {
        // given
        SlackMessage message = SlackMessage.builder()
                .title("")
                .message("예외 메시지")
                .build();

        // when
        String formatted = SlackMessageFormatter.makeExceptionMessage(message);

        // then
        assertThat(formatted).isNotNull();
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(formatted);
        assertThat(json).isNotNull();
    }

    @Test
    void 예외_메시지_포맷팅_빈_메시지() throws Exception {
        // given
        SlackMessage message = SlackMessage.builder()
                .title("제목")
                .message("")
                .build();

        // when
        String formatted = SlackMessageFormatter.makeExceptionMessage(message);

        // then
        assertThat(formatted).isNotNull();
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(formatted);
        assertThat(json).isNotNull();
    }

    @Test
    void 예외_메시지_포맷팅_긴_메시지() throws Exception {
        // given
        String longMessage = "a".repeat(1000);
        SlackMessage message = SlackMessage.builder()
                .title("제목")
                .message(longMessage)
                .build();

        // when
        String formatted = SlackMessageFormatter.makeExceptionMessage(message);

        // then
        assertThat(formatted).isNotNull();
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(formatted);
        assertThat(json).isNotNull();
    }

    @Test
    void 예외_메시지_포맷팅_특수문자_포함() throws Exception {
        // given
        SlackMessage message = SlackMessage.builder()
                .title("제목!@#$%")
                .message("메시지 <script>alert('test')</script>")
                .build();

        // when
        String formatted = SlackMessageFormatter.makeExceptionMessage(message);

        // then
        assertThat(formatted).isNotNull();
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(formatted);
        assertThat(json).isNotNull();
    }

    @Test
    void 예외_메시지_포맷팅_한글_포함() throws Exception {
        // given
        SlackMessage message = SlackMessage.builder()
                .title("한글 제목")
                .message("한글 메시지 내용입니다")
                .build();

        // when
        String formatted = SlackMessageFormatter.makeExceptionMessage(message);

        // then
        assertThat(formatted).isNotNull();
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(formatted);
        assertThat(json).isNotNull();
    }
}

