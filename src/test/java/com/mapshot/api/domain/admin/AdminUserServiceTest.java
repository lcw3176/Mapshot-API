package com.mapshot.api.domain.admin;

import com.mapshot.api.domain.admin.user.AdminUserEntity;
import com.mapshot.api.domain.admin.user.AdminUserRepository;
import com.mapshot.api.domain.admin.user.AdminUserService;
import com.mapshot.api.domain.community.post.PostEntity;
import com.mapshot.api.domain.community.post.PostRepository;
import com.mapshot.api.domain.notice.NoticeService;
import com.mapshot.api.domain.notice.NoticeType;
import com.mapshot.api.infra.encrypt.EncryptUtil;
import com.mapshot.api.infra.exception.ApiException;
import com.mapshot.api.infra.exception.status.ErrorCode;
import com.mapshot.api.presentation.notice.NoticeRegistrationRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class AdminUserServiceTest {

    @Autowired
    private AdminUserService adminUserService;

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private AdminUserRepository adminUserRepository;

    @Autowired
    private PostRepository postRepository;


    @BeforeEach
    void init() {
        adminUserRepository.save(AdminUserEntity.builder()
                .nickname("test")
                .password(EncryptUtil.encrypt("1234"))
                .build());
    }

    @AfterEach
    void release() {
        adminUserRepository.deleteAll();
    }


    @Test
    void 로그인_테스트() {
        assertThatNoException().isThrownBy(() -> adminUserService.validateUser("test", "1234"));
    }

    @Test
    void 존재하지_않는_유저로_로그인시_예외_발생() {
        assertThatThrownBy(() -> adminUserService.validateUser("i am not exist", "1234"))
                .isInstanceOf(ApiException.class)
                .hasMessage(ErrorCode.NO_SUCH_USER.getMessage());
    }


    @Test
    void 게시글_관리자_권한으로_삭제() {
        long id = postRepository.save(PostEntity.builder()
                .title("hello")
                .writer("good")
                .content("hello")
                .commentCount(0)
                .password(EncryptUtil.encrypt("1234"))
                .build()).getId();

        assertThatNoException().isThrownBy(() -> adminUserService.deletePost(id));
    }


    @Test
    void 공지사항_저장_테스트() {

        long id = adminUserService.saveNotice(NoticeType.FIX, "헬로", "방가방가");

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

        long id = adminUserService.saveNotice(NoticeType.UPDATE, "초기화", "초기화");
        long updatedId = adminUserService.modifyNotice(id, NoticeType.FIX, "헬로", "헬로");


        assertEquals(id, updatedId);

        String noticeType = noticeService.getSinglePost(updatedId).getNoticeType();
        assertEquals(NoticeType.FIX.getKorean(), noticeType);
    }

    @Test
    void 없는_데이터_수정시_예외_발생() {

        long id = adminUserService.saveNotice(NoticeType.UPDATE, "초기화", "초기화");

        assertThatThrownBy(() ->
                adminUserService.modifyNotice(id + 1, NoticeType.FIX, "헬로", "헬로"))
                .isInstanceOf(ApiException.class)
                .hasMessage(ErrorCode.NO_SUCH_NOTICE.getMessage());

    }


    @Test
    void 삭제_테스트() {

        long id = adminUserService.saveNotice(NoticeType.UPDATE, "초기화", "초기화");

        assertThatNoException()
                .isThrownBy(() -> adminUserService.deleteNotice(id));

    }


    @Test
    void 없는_데이터_삭제_요청시_예외_발생() {

        long id = adminUserService.saveNotice(NoticeType.UPDATE, "초기화", "초기화");

        assertThatThrownBy(() -> adminUserService.deleteNotice(id + 1))
                .isInstanceOf(ApiException.class)
                .hasMessage(ErrorCode.NO_SUCH_NOTICE.getMessage());
    }


}
