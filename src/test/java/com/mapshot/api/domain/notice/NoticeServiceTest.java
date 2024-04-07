package com.mapshot.api.domain.notice;

import com.mapshot.api.infra.exception.ApiException;
import com.mapshot.api.infra.exception.status.ErrorCode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Comparator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class NoticeServiceTest {

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private NoticeRepository noticeRepository;

    @Value("${notice.post.page_size}")
    private int totalSearchSize;
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
    void 시작_page를_0으로_전송_시_가장_최근_게시글부터_10개_반환() {
        int page = 0;

        NoticeListResponse lst = noticeService.getNoticeByPageNumber(page);
        assertThat(lst.getNotices()).hasSize(totalSearchSize)
                .isSortedAccordingTo(Comparator.comparing(NoticeDto::getId).reversed());

    }

    @Test
    void 시작_page를_1으로_전송_시_가장_최근_게시글부터_10개_반환() {
        int page = 1;

        NoticeListResponse lst = noticeService.getNoticeByPageNumber(page);
        assertThat(lst.getNotices()).hasSize(totalSearchSize)
                .isSortedAccordingTo(Comparator.comparing(NoticeDto::getId).reversed());

    }


    @Test
    void 단일_공지사항_가져오기() {

        long id = noticeRepository.findFirstByOrderByIdDesc().getId();

        assertThat(noticeService.getSinglePost(id))
                .isNotNull()
                .extracting(NoticeDetailResponse::getId)
                .isEqualTo(id);

    }

    @Test
    void 존재하지_않는_공지사항을_가져올_시_예외발생() {
        assertThatThrownBy(() -> noticeService.getSinglePost(-1))
                .isInstanceOf(ApiException.class)
                .hasMessageStartingWith(ErrorCode.NO_SUCH_NOTICE.getMessage());

    }


    @Test
    void _0미만의_페이지값_전송_시_가장_최근_게시글부터_10개_반환() {
        int page = -100;

        NoticeListResponse lst = noticeService.getNoticeByPageNumber(page);
        assertThat(lst.getNotices()).hasSize(10)
                .isSortedAccordingTo(Comparator.comparing(NoticeDto::getId).reversed());

    }


}
