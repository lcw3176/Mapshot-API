package com.joebrooks.mapshotapi.repository.notice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
    @ValueSource(longs = {1, 2, 3, 4, 5, 6, 7})
    void 단일_공지사항_가져오기(long id) {
        assertThat(noticeService.getPost(id))
                .isNotNull()
                .extracting(NoticeEntity::getId)
                .isEqualTo(id);

    }

    @Test
    void 존재하지_않는_공지사항을_가져올_시_예외발생() {
        assertThatThrownBy(() -> noticeService.getPost(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageStartingWith("[ERROR]");

    }


    @ParameterizedTest
    @ValueSource(longs = {12, 23, 34, 15, 56, 77})
    void 데이터_갯수가_충분한_여러개의_공지사항_가져오기(long id) {
        int size = 10;

        List<NoticeEntity> lst = noticeService.getPosts(id);
        assertThat(lst).hasSize(size)
                .isSortedAccordingTo(Comparator.comparing(NoticeEntity::getId).reversed());

    }

    @Test
    void 데이터_갯수가_모자란_여러개의_공지사항_가져오기() {
        int id = 4;

        List<NoticeEntity> lst = noticeService.getPosts(id);
        assertThat(lst).hasSize(id)
                .isSortedAccordingTo(Comparator.comparing(NoticeEntity::getId).reversed());

    }
}