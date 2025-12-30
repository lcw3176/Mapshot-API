package com.mapshot.api.application.notice;

import com.mapshot.api.domain.notice.Notice;
import com.mapshot.api.domain.notice.NoticeType;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class NoticeDtoTest {

    @Test
    void NoticeDto_fromEntity_변환_성공() {
        // given
        Notice notice = Notice.builder()
                .id(1L)
                .title("테스트 제목")
                .content("테스트 내용")
                .noticeType(NoticeType.UPDATE)
                .build();

        // when
        NoticeDto dto = NoticeDto.fromEntity(notice);

        // then
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getTitle()).isEqualTo("테스트 제목");
        assertThat(dto.getNoticeType()).isEqualTo("업데이트");
    }

    @Test
    void NoticeDto_빌더_패턴_테스트() {
        // given
        LocalDateTime now = LocalDateTime.now();

        // when
        NoticeDto dto = NoticeDto.builder()
                .id(1L)
                .title("제목")
                .noticeType("업데이트")
                .createdDate(now)
                .build();

        // then
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getTitle()).isEqualTo("제목");
        assertThat(dto.getNoticeType()).isEqualTo("업데이트");
        assertThat(dto.getCreatedDate()).isEqualTo(now);
    }

    @Test
    void NoticeDto_모든_타입_변환_테스트() {
        // given
        Notice update = Notice.builder()
                .id(1L)
                .title("제목")
                .noticeType(NoticeType.UPDATE)
                .build();

        Notice fix = Notice.builder()
                .id(2L)
                .title("제목")
                .noticeType(NoticeType.FIX)
                .build();

        Notice reserved = Notice.builder()
                .id(3L)
                .title("제목")
                .noticeType(NoticeType.RESERVED_CHECK)
                .build();

        // when
        NoticeDto updateDto = NoticeDto.fromEntity(update);
        NoticeDto fixDto = NoticeDto.fromEntity(fix);
        NoticeDto reservedDto = NoticeDto.fromEntity(reserved);

        // then
        assertThat(updateDto.getNoticeType()).isEqualTo("업데이트");
        assertThat(fixDto.getNoticeType()).isEqualTo("오류수정");
        assertThat(reservedDto.getNoticeType()).isEqualTo("점검예정");
    }
}

