package com.mapshot.api.application.notice;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class NoticeDetailResponseTest {

    @Test
    void NoticeDetailResponse_생성_성공() {
        // given
        LocalDateTime now = LocalDateTime.now();

        // when
        NoticeDetailResponse response = NoticeDetailResponse.builder()
                .id(1L)
                .noticeType("업데이트")
                .title("테스트 제목")
                .content("테스트 내용")
                .createdDate(now)
                .build();

        // then
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getNoticeType()).isEqualTo("업데이트");
        assertThat(response.getTitle()).isEqualTo("테스트 제목");
        assertThat(response.getContent()).isEqualTo("테스트 내용");
        assertThat(response.getCreatedDate()).isEqualTo(now);
    }

    @Test
    void NoticeDetailResponse_빌더_패턴_테스트() {
        // when
        NoticeDetailResponse response = NoticeDetailResponse.builder()
                .id(1L)
                .noticeType("오류수정")
                .title("제목")
                .content("내용")
                .createdDate(LocalDateTime.now())
                .build();

        // then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getNoticeType()).isEqualTo("오류수정");
    }

    @Test
    void NoticeDetailResponse_기본_생성자_테스트() {
        // when
        NoticeDetailResponse response = new NoticeDetailResponse();

        // then
        assertThat(response).isNotNull();
    }

    @Test
    void NoticeDetailResponse_전체_생성자_테스트() {
        // given
        LocalDateTime now = LocalDateTime.now();

        // when
        NoticeDetailResponse response = new NoticeDetailResponse(1L, "업데이트", "제목", "내용", now);

        // then
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getNoticeType()).isEqualTo("업데이트");
        assertThat(response.getTitle()).isEqualTo("제목");
        assertThat(response.getContent()).isEqualTo("내용");
        assertThat(response.getCreatedDate()).isEqualTo(now);
    }
}

