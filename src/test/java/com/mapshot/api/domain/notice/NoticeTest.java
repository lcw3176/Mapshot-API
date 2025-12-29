package com.mapshot.api.domain.notice;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NoticeTest {

    @Test
    void 공지사항_생성_성공() {
        // given
        String title = "테스트 제목";
        String content = "테스트 내용";
        NoticeType noticeType = NoticeType.UPDATE;

        // when
        Notice notice = Notice.builder()
                .title(title)
                .content(content)
                .noticeType(noticeType)
                .build();

        // then
        assertThat(notice.getTitle()).isEqualTo(title);
        assertThat(notice.getContent()).isEqualTo(content);
        assertThat(notice.getNoticeType()).isEqualTo(noticeType);
    }

    @Test
    void 공지사항_업데이트_성공() {
        // given
        Notice notice = Notice.builder()
                .title("원래 제목")
                .content("원래 내용")
                .noticeType(NoticeType.UPDATE)
                .build();

        String newTitle = "새 제목";
        String newContent = "새 내용";
        NoticeType newType = NoticeType.FIX;

        // when
        notice.update(newTitle, newType, newContent);

        // then
        assertThat(notice.getTitle()).isEqualTo(newTitle);
        assertThat(notice.getContent()).isEqualTo(newContent);
        assertThat(notice.getNoticeType()).isEqualTo(newType);
    }

    @Test
    void 공지사항_업데이트_모든_타입_테스트() {
        // given
        Notice notice = Notice.builder()
                .title("제목")
                .content("내용")
                .noticeType(NoticeType.UPDATE)
                .build();

        // when & then - UPDATE
        notice.update("제목1", NoticeType.UPDATE, "내용1");
        assertThat(notice.getNoticeType()).isEqualTo(NoticeType.UPDATE);

        // when & then - FIX
        notice.update("제목2", NoticeType.FIX, "내용2");
        assertThat(notice.getNoticeType()).isEqualTo(NoticeType.FIX);

        // when & then - RESERVED_CHECK
        notice.update("제목3", NoticeType.RESERVED_CHECK, "내용3");
        assertThat(notice.getNoticeType()).isEqualTo(NoticeType.RESERVED_CHECK);
    }

    @Test
    void 공지사항_빌더_패턴_테스트() {
        // when
        Notice notice = Notice.builder()
                .title("제목")
                .content("내용")
                .noticeType(NoticeType.UPDATE)
                .build();

        // then
        assertThat(notice).isNotNull();
        assertThat(notice.getTitle()).isEqualTo("제목");
        assertThat(notice.getContent()).isEqualTo("내용");
        assertThat(notice.getNoticeType()).isEqualTo(NoticeType.UPDATE);
    }
}

