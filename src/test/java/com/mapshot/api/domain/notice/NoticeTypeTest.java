package com.mapshot.api.domain.notice;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NoticeTypeTest {

    @Test
    void 공지사항_타입_값_테스트() {
        // when & then
        assertThat(NoticeType.UPDATE.getKorean()).isEqualTo("업데이트");
        assertThat(NoticeType.FIX.getKorean()).isEqualTo("오류수정");
        assertThat(NoticeType.RESERVED_CHECK.getKorean()).isEqualTo("점검예정");
    }

    @Test
    void 공지사항_타입_모든_값_존재_확인() {
        // when
        NoticeType[] types = NoticeType.values();

        // then
        assertThat(types).hasSize(3);
        assertThat(types).contains(NoticeType.UPDATE, NoticeType.FIX, NoticeType.RESERVED_CHECK);
    }

    @Test
    void 공지사항_타입_이름으로_찾기() {
        // when
        NoticeType update = NoticeType.valueOf("UPDATE");
        NoticeType fix = NoticeType.valueOf("FIX");
        NoticeType reservedCheck = NoticeType.valueOf("RESERVED_CHECK");

        // then
        assertThat(update).isEqualTo(NoticeType.UPDATE);
        assertThat(fix).isEqualTo(NoticeType.FIX);
        assertThat(reservedCheck).isEqualTo(NoticeType.RESERVED_CHECK);
    }
}

