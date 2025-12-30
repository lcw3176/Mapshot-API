package com.mapshot.api.infra.client.slack.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SlackMessageTest {

    @Test
    void SlackMessage_생성_성공() {
        // when
        SlackMessage message = SlackMessage.builder()
                .title("테스트 제목")
                .message("테스트 메시지")
                .build();

        // then
        assertThat(message.getTitle()).isEqualTo("테스트 제목");
        assertThat(message.getMessage()).isEqualTo("테스트 메시지");
    }

    @Test
    void SlackMessage_빌더_패턴_테스트() {
        // when
        SlackMessage message = SlackMessage.builder()
                .title("에러 제목")
                .message("에러 메시지 내용")
                .build();

        // then
        assertThat(message).isNotNull();
        assertThat(message.getTitle()).isEqualTo("에러 제목");
        assertThat(message.getMessage()).isEqualTo("에러 메시지 내용");
    }

    @Test
    void SlackMessage_기본_생성자_테스트() {
        // when
        SlackMessage message = new SlackMessage();

        // then
        assertThat(message).isNotNull();
    }

    @Test
    void SlackMessage_전체_생성자_테스트() {
        // when
        SlackMessage message = new SlackMessage("제목", "메시지");

        // then
        assertThat(message.getTitle()).isEqualTo("제목");
        assertThat(message.getMessage()).isEqualTo("메시지");
    }
}

