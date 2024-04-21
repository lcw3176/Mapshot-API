package com.mapshot.api.domain.admin.notice;

import com.mapshot.api.domain.notice.NoticeService;
import com.mapshot.api.domain.notice.NoticeType;
import com.mapshot.api.infra.exception.ApiException;
import com.mapshot.api.infra.exception.status.ErrorCode;
import com.mapshot.api.presentation.admin.AdminNoticeRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class AdminNoticeServiceTest {

    @Autowired
    private AdminNoticeService adminNoticeService;

    @Autowired
    private NoticeService noticeService;


    @Test
    void 공지사항_저장_테스트() {

        long id = adminNoticeService.saveNotice(NoticeType.FIX, "헬로", "방가방가");

        long savedId = noticeService.getSinglePost(id).getId();

        assertEquals(id, savedId);
    }

    @Test
    void 업데이트_테스트() {

        AdminNoticeRequest request = AdminNoticeRequest.builder()
                .noticeType(NoticeType.UPDATE.toString())
                .title("초기화")
                .content("초기화")
                .build();

        long id = adminNoticeService.saveNotice(NoticeType.UPDATE, "초기화", "초기화");
        long updatedId = adminNoticeService.modifyNotice(id, NoticeType.FIX, "헬로", "헬로");


        assertEquals(id, updatedId);

        String noticeType = noticeService.getSinglePost(updatedId).getNoticeType();
        assertEquals(NoticeType.FIX.getKorean(), noticeType);
    }

    @Test
    void 없는_데이터_수정시_예외_발생() {

        long id = adminNoticeService.saveNotice(NoticeType.UPDATE, "초기화", "초기화");

        assertThatThrownBy(() ->
                adminNoticeService.modifyNotice(id + 1, NoticeType.FIX, "헬로", "헬로"))
                .isInstanceOf(ApiException.class)
                .hasMessage(ErrorCode.NO_SUCH_NOTICE.getMessage());

    }


    @Test
    void 삭제_테스트() {

        long id = adminNoticeService.saveNotice(NoticeType.UPDATE, "초기화", "초기화");

        assertThatNoException()
                .isThrownBy(() -> adminNoticeService.deleteNotice(id));

    }


    @Test
    void 없는_데이터_삭제_요청시_예외_발생() {

        long id = adminNoticeService.saveNotice(NoticeType.UPDATE, "초기화", "초기화");

        assertThatThrownBy(() -> adminNoticeService.deleteNotice(id + 1))
                .isInstanceOf(ApiException.class)
                .hasMessage(ErrorCode.NO_SUCH_NOTICE.getMessage());
    }


}