package com.joebrooks.mapshotapi.notice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.joebrooks.mapshotapi.notice.model.PostDetailResponse;
import com.joebrooks.mapshotapi.notice.model.PostSummaryResponse;
import java.util.Comparator;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class NoticeServiceTest {

    @Autowired
    private NoticeService noticeService;


    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3, 4, 5})
    void 단일_공지사항_가져오기(long id) {
        assertThat(noticeService.getSinglePost(id))
                .isNotNull()
                .extracting(PostDetailResponse::getId)
                .isEqualTo(id);

    }

    @Test
    void 존재하지_않는_공지사항을_가져올_시_예외발생() {
        assertThatThrownBy(() -> noticeService.getSinglePost(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageStartingWith("[ERROR]");

    }


    @Test
    void 데이터_갯수가_충분한_여러개의_공지사항_가져오기() {
        long id = 12;
        int size = 10;

        List<PostSummaryResponse> lst = noticeService.getMultiplePostsSummary(id);
        assertThat(lst).hasSize(size)
                .isSortedAccordingTo(Comparator.comparing(PostSummaryResponse::getId).reversed());

    }

    @Test
    void 데이터_갯수가_모자란_여러개의_공지사항_가져오기() {
        int id = 4;
        int size = 3;
        List<PostSummaryResponse> lst = noticeService.getMultiplePostsSummary(id);
        assertThat(lst).hasSize(size)
                .isSortedAccordingTo(Comparator.comparing(PostSummaryResponse::getId).reversed());

    }


    @Test
    void 시작_id를_0으로_전송_시_가장_최근_게시글부터_10개_반환() {
        int id = 0;

        List<PostSummaryResponse> lst = noticeService.getMultiplePostsSummary(id);
        assertThat(lst).hasSize(10)
                .isSortedAccordingTo(Comparator.comparing(PostSummaryResponse::getId).reversed());

    }
}