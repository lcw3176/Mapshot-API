package com.mapshot.api.domain.notice;

import com.mapshot.api.infra.exception.ApiException;
import com.mapshot.api.infra.exception.status.ErrorCode;
import com.mapshot.api.presentation.notice.model.NoticeDetailResponse;
import com.mapshot.api.presentation.notice.model.NoticeListResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class NoticeServiceTest {

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private NoticeRepository noticeRepository;
    private static final int totalSearchSize = 20;
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
    void 시작_id를_0으로_전송_시_가장_최근_게시글부터_10개_반환() {
        int id = 0;

        List<NoticeListResponse> lst = noticeService.getNoticeList(id);
        assertThat(lst).hasSize(totalSearchSize)
                .isSortedAccordingTo(Comparator.comparing(NoticeListResponse::getId).reversed());

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
    void 데이터_갯수가_충분한_여러개의_공지사항_가져오기() {
        long id = noticeRepository.findFirstByOrderByIdDesc().getId() + 1;

        List<NoticeListResponse> lst = noticeService.getNoticeList(id);
        assertThat(lst).hasSize(totalSearchSize)
                .isSortedAccordingTo(Comparator.comparing(NoticeListResponse::getId).reversed());

    }

    @Test
    void 데이터_갯수가_모자란_여러개의_공지사항_가져오기() {
        int size = 3;
        long id = noticeRepository.findFirstByOrderByIdDesc().getId() - (testDataSize - size - 1);

        List<NoticeListResponse> lst = noticeService.getNoticeList(id);
        assertThat(lst).hasSize(size)
                .isSortedAccordingTo(Comparator.comparing(NoticeListResponse::getId).reversed());

    }


}
