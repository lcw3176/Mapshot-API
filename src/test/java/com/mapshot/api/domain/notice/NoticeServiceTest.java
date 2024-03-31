package com.mapshot.api.domain.notice;

import com.mapshot.api.infra.exception.ApiException;
import com.mapshot.api.infra.exception.status.ErrorCode;
import com.mapshot.api.presentation.notice.model.NoticeDetailResponse;
import com.mapshot.api.presentation.notice.model.NoticeListResponse;
import com.mapshot.api.presentation.notice.model.NoticeRegistrationRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class NoticeServiceTest {

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private NoticeRepository noticeRepository;
    private static final int totalSearchSize = 20;

    @BeforeEach
    void init() {
        for (int i = 0; i < 40; i++) {
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
    void 저장_테스트() {

        NoticeRegistrationRequest request = NoticeRegistrationRequest.builder()
                .noticeType(NoticeType.FIX.toString())
                .title("헬로")
                .content("방가방가")
                .build();

        long id = noticeService.save(request);

        long savedId = noticeService.getSinglePost(id).getId();

        assertEquals(id, savedId);
    }

    @Test
    void 업데이트_테스트() {

        NoticeRegistrationRequest request = NoticeRegistrationRequest.builder()
                .noticeType(NoticeType.UPDATE.toString())
                .title("초기화")
                .content("초기화")
                .build();

        long id = noticeService.save(request);

        long updatedId = noticeService.modify(id,
                NoticeRegistrationRequest.builder()
                        .noticeType(NoticeType.FIX.toString())
                        .title("헬로")
                        .content("헬로")
                        .build());


        assertEquals(id, updatedId);

        String noticeType = noticeService.getSinglePost(updatedId).getNoticeType();
        assertEquals(NoticeType.FIX.getKorean(), noticeType);
    }

    @Test
    void 없는_데이터_수정시_예외_발생() {

        NoticeRegistrationRequest request = NoticeRegistrationRequest.builder()
                .noticeType(NoticeType.UPDATE.toString())
                .title("초기화")
                .content("초기화")
                .build();

        long id = noticeService.save(request);

        assertThatThrownBy(() ->
                noticeService.modify(id + 1,
                        NoticeRegistrationRequest.builder()
                                .noticeType(NoticeType.FIX.toString())
                                .title("헬로")
                                .content("헬로")
                                .build()))
                .isInstanceOf(ApiException.class)
                .hasMessage(ErrorCode.NO_SUCH_NOTICE.getMessage());

    }


    @Test
    void 삭제_테스트() {

        NoticeRegistrationRequest request = NoticeRegistrationRequest.builder()
                .noticeType(NoticeType.UPDATE.toString())
                .title("초기화")
                .content("초기화")
                .build();

        long id = noticeService.save(request);

        assertThatNoException()
                .isThrownBy(() -> noticeService.delete(id));

    }


    @Test
    void 없는_데이터_삭제_요청시_예외_발생() {

        NoticeRegistrationRequest request = NoticeRegistrationRequest.builder()
                .noticeType(NoticeType.UPDATE.toString())
                .title("초기화")
                .content("초기화")
                .build();

        long id = noticeService.save(request);

        assertThatThrownBy(() -> noticeService.delete(id + 1))
                .isInstanceOf(ApiException.class)
                .hasMessage(ErrorCode.NO_SUCH_NOTICE.getMessage());
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
        long id = noticeRepository.findFirstByOrderByIdDesc().getId() - (totalSearchSize - size - 1);

        List<NoticeListResponse> lst = noticeService.getNoticeList(id);
        assertThat(lst).hasSize(size)
                .isSortedAccordingTo(Comparator.comparing(NoticeListResponse::getId).reversed());

    }


}
