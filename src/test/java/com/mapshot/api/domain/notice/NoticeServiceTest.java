package com.mapshot.api.domain.notice;

import com.mapshot.api.infra.exception.ApiException;
import com.mapshot.api.infra.exception.status.ErrorCode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import java.util.Comparator;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

        Page<NoticeEntity> lst = noticeService.findByPageNumber(page);
        assertThat(lst.getContent()).hasSize(totalSearchSize)
                .isSortedAccordingTo(Comparator.comparing(NoticeEntity::getId).reversed());

    }

    @Test
    void 시작_page를_1으로_전송_시_가장_최근_게시글부터_10개_반환() {
        int page = 1;

        Page<NoticeEntity> lst = noticeService.findByPageNumber(page);
        assertThat(lst.getContent()).hasSize(totalSearchSize)
                .isSortedAccordingTo(Comparator.comparing(NoticeEntity::getId).reversed());

    }


    @Test
    void 단일_공지사항_가져오기() {

        long id = noticeRepository.findFirstByOrderByIdDesc().getId();

        assertThat(noticeService.findById(id))
                .isNotNull()
                .extracting(NoticeEntity::getId)
                .isEqualTo(id);

    }

    @Test
    void 존재하지_않는_공지사항을_가져올_시_예외발생() {
        assertThatThrownBy(() -> noticeService.findById(-1))
                .isInstanceOf(ApiException.class)
                .hasMessageStartingWith(ErrorCode.NO_SUCH_NOTICE.getMessage());

    }


    @Test
    void _0미만의_페이지값_전송_시_가장_최근_게시글부터_10개_반환() {
        int page = -100;

        Page<NoticeEntity> lst = noticeService.findByPageNumber(page);
        assertThat(lst.getContent()).hasSize(10)
                .isSortedAccordingTo(Comparator.comparing(NoticeEntity::getId).reversed());

    }


    @Test
    void 공지사항_저장_테스트() {

        long id = noticeService.save(NoticeType.FIX, "헬로", "방가방가");

        long savedId = noticeService.findById(id).getId();

        assertEquals(id, savedId);
    }

    @Test
    void 업데이트_테스트() {


        long id = noticeService.save(NoticeType.UPDATE, "초기화", "초기화");
        long updatedId = noticeService.update(id, NoticeType.FIX, "헬로", "헬로");


        assertEquals(id, updatedId);

        NoticeType noticeType = noticeService.findById(updatedId).getNoticeType();
        assertEquals(NoticeType.FIX.getKorean(), noticeType.getKorean());
    }

    @Test
    void 없는_데이터_수정시_예외_발생() {

        long id = noticeService.save(NoticeType.UPDATE, "초기화", "초기화");

        assertThatThrownBy(() ->
                noticeService.update(id + 1, NoticeType.FIX, "헬로", "헬로"))
                .isInstanceOf(ApiException.class)
                .hasMessage(ErrorCode.NO_SUCH_NOTICE.getMessage());

    }


    @Test
    void 삭제_테스트() {

        long id = noticeService.save(NoticeType.UPDATE, "초기화", "초기화");

        assertThatNoException()
                .isThrownBy(() -> noticeService.delete(id));

    }


    @Test
    void 없는_데이터_삭제_요청시_예외_발생() {

        long id = noticeService.save(NoticeType.UPDATE, "초기화", "초기화");

        assertThatThrownBy(() -> noticeService.delete(id + 1))
                .isInstanceOf(ApiException.class)
                .hasMessage(ErrorCode.NO_SUCH_NOTICE.getMessage());
    }


}
