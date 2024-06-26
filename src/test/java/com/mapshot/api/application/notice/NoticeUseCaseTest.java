package com.mapshot.api.application.notice;

import com.mapshot.api.domain.notice.NoticeEntity;
import com.mapshot.api.domain.notice.NoticeRepository;
import com.mapshot.api.domain.notice.NoticeType;
import com.mapshot.api.infra.exception.ApiException;
import com.mapshot.api.infra.exception.status.ErrorCode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class NoticeUseCaseTest {

    @Autowired
    private NoticeUseCase noticeUseCase;

    @Autowired
    private NoticeRepository noticeRepository;

    @Value("${notice.post.page_size}")
    private int pageSize;
    private static final int testDataSize = 40;

    @BeforeEach
    void init() {
        for (int i = 0; i < testDataSize; i++) {
            noticeRepository.save(NoticeEntity.builder()
                    .noticeType(NoticeType.UPDATE)
                    .title(Integer.toString(i))
                    .content(Integer.toString(i))
                    .build());
        }
    }


    @AfterEach
    void release() {
        noticeRepository.deleteAll();
    }


    @Test
    void 단일_공지사항_가져오기() {
        NoticeEntity notice = noticeRepository.findFirstByOrderByIdDesc();

        NoticeDetailResponse response = noticeUseCase.getNotice(notice.getId());

        assertEquals(response.getContent(), notice.getContent());
        assertEquals(response.getNoticeType(), notice.getNoticeType().getKorean());
        assertEquals(response.getTitle(), notice.getTitle());
        assertEquals(response.getCreatedDate(), notice.getCreatedDate());
    }

    @Test
    void 없는_공지사항_가져오면_예외() {
        assertThatThrownBy(() -> noticeUseCase.getNotice(-1))
                .isInstanceOf(ApiException.class)
                .hasMessage(ErrorCode.NO_SUCH_NOTICE.getMessage());
    }

    @Test
    void 공지사항_목록_가져오기() {
        NoticeListResponse response = noticeUseCase.getNoticeList(1);

        assertEquals(response.getTotalPage(), testDataSize / pageSize);
        assertEquals(response.getNotices().size(), pageSize);
    }

    @Test
    void _0이하의_페이지_조회시_첫번째_페이지_목록_반환() {
        NoticeListResponse response = noticeUseCase.getNoticeList(-1);

        assertEquals(response.getTotalPage(), testDataSize / pageSize);
        assertEquals(response.getNotices().size(), pageSize);
    }
}