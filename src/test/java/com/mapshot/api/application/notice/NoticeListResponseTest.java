package com.mapshot.api.application.notice;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class NoticeListResponseTest {

    @Test
    void NoticeListResponse_생성_성공() {
        // given
        NoticeDto dto1 = NoticeDto.builder()
                .id(1L)
                .title("제목1")
                .noticeType("업데이트")
                .build();

        NoticeDto dto2 = NoticeDto.builder()
                .id(2L)
                .title("제목2")
                .noticeType("오류수정")
                .build();

        List<NoticeDto> notices = Arrays.asList(dto1, dto2);

        // when
        NoticeListResponse response = NoticeListResponse.builder()
                .notices(notices)
                .totalPage(5)
                .build();

        // then
        assertThat(response.getNotices()).hasSize(2);
        assertThat(response.getTotalPage()).isEqualTo(5);
    }

    @Test
    void NoticeListResponse_빌더_패턴_테스트() {
        // when
        NoticeListResponse response = NoticeListResponse.builder()
                .notices(List.of())
                .totalPage(0)
                .build();

        // then
        assertThat(response).isNotNull();
        assertThat(response.getNotices()).isEmpty();
        assertThat(response.getTotalPage()).isEqualTo(0);
    }

    @Test
    void NoticeListResponse_기본_생성자_테스트() {
        // when
        NoticeListResponse response = new NoticeListResponse();

        // then
        assertThat(response).isNotNull();
    }

    @Test
    void NoticeListResponse_전체_생성자_테스트() {
        // given
        List<NoticeDto> notices = List.of(
                NoticeDto.builder().id(1L).title("제목1").build(),
                NoticeDto.builder().id(2L).title("제목2").build()
        );

        // when
        NoticeListResponse response = new NoticeListResponse(notices, 3);

        // then
        assertThat(response.getNotices()).hasSize(2);
        assertThat(response.getTotalPage()).isEqualTo(3);
    }
}

