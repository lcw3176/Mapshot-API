package com.mapshot.api.notice.service;

import com.mapshot.api.common.exception.ApiException;
import com.mapshot.api.common.exception.status.ErrorCode;
import com.mapshot.api.notice.enums.NoticeType;
import com.mapshot.api.notice.model.NoticeDetailResponse;
import com.mapshot.api.notice.model.NoticeRequest;
import com.mapshot.api.notice.model.NoticeSummaryResponse;
import com.mapshot.api.notice.repository.NoticeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class NoticeServiceTest {

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private NoticeRepository noticeRepository;

    @AfterEach
    void release() {
        noticeRepository.deleteAll();
    }


    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3, 4, 5})
    void 단일_공지사항_가져오기(long id) {
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
        long id = 12;
        int size = 10;

        List<NoticeSummaryResponse> lst = noticeService.getMultiplePostsSummary(id);
        assertThat(lst).hasSize(size)
                .isSortedAccordingTo(Comparator.comparing(NoticeSummaryResponse::getId).reversed());

    }

    @Test
    void 데이터_갯수가_모자란_여러개의_공지사항_가져오기() {
        int id = 4;
        int size = 3;
        List<NoticeSummaryResponse> lst = noticeService.getMultiplePostsSummary(id);
        assertThat(lst).hasSize(size)
                .isSortedAccordingTo(Comparator.comparing(NoticeSummaryResponse::getId).reversed());

    }


    @Test
    void 시작_id를_0으로_전송_시_가장_최근_게시글부터_10개_반환() {
        int id = 0;

        List<NoticeSummaryResponse> lst = noticeService.getMultiplePostsSummary(id);
        assertThat(lst).hasSize(10)
                .isSortedAccordingTo(Comparator.comparing(NoticeSummaryResponse::getId).reversed());

    }


    @Test
    void 저장_테스트() {

        NoticeRequest request = NoticeRequest.builder()
                .noticeType(NoticeType.FIX.toString())
                .title("헬로")
                .content("방가방가")
                .build();

        long id = noticeService.save(request);

        long savedId = noticeService.getSinglePost(id).getId();

        assertEquals(id, savedId);
    }

    
}