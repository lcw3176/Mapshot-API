package com.mapshot.api.presentation.admin;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AdminNoticeRequestTest {

    @Test
    void AdminNoticeRequest_생성_성공() {
        // when
        AdminNoticeRequest request = AdminNoticeRequest.builder()
                .noticeType("UPDATE")
                .title("테스트 제목")
                .content("테스트 내용")
                .build();

        // then
        assertThat(request.getNoticeType()).isEqualTo("UPDATE");
        assertThat(request.getTitle()).isEqualTo("테스트 제목");
        assertThat(request.getContent()).isEqualTo("테스트 내용");
    }

    @Test
    void AdminNoticeRequest_빌더_패턴_테스트() {
        // when
        AdminNoticeRequest request = AdminNoticeRequest.builder()
                .noticeType("FIX")
                .title("오류 수정")
                .content("버그 수정 내용")
                .build();

        // then
        assertThat(request).isNotNull();
        assertThat(request.getNoticeType()).isEqualTo("FIX");
        assertThat(request.getTitle()).isEqualTo("오류 수정");
        assertThat(request.getContent()).isEqualTo("버그 수정 내용");
    }

    @Test
    void AdminNoticeRequest_기본_생성자_테스트() {
        // when
        AdminNoticeRequest request = new AdminNoticeRequest();

        // then
        assertThat(request).isNotNull();
    }

    @Test
    void AdminNoticeRequest_전체_생성자_테스트() {
        // when
        AdminNoticeRequest request = new AdminNoticeRequest("RESERVED_CHECK", "점검 예정", "점검 내용");

        // then
        assertThat(request.getNoticeType()).isEqualTo("RESERVED_CHECK");
        assertThat(request.getTitle()).isEqualTo("점검 예정");
        assertThat(request.getContent()).isEqualTo("점검 내용");
    }

    @Test
    void AdminNoticeRequest_모든_타입_테스트() {
        // when & then
        AdminNoticeRequest update = AdminNoticeRequest.builder()
                .noticeType("UPDATE")
                .title("업데이트")
                .content("내용")
                .build();
        assertThat(update.getNoticeType()).isEqualTo("UPDATE");

        AdminNoticeRequest fix = AdminNoticeRequest.builder()
                .noticeType("FIX")
                .title("수정")
                .content("내용")
                .build();
        assertThat(fix.getNoticeType()).isEqualTo("FIX");

        AdminNoticeRequest reserved = AdminNoticeRequest.builder()
                .noticeType("RESERVED_CHECK")
                .title("점검")
                .content("내용")
                .build();
        assertThat(reserved.getNoticeType()).isEqualTo("RESERVED_CHECK");
    }
}

