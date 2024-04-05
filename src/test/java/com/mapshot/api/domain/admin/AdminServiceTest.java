package com.mapshot.api.domain.admin;

import com.mapshot.api.domain.community.post.PostEntity;
import com.mapshot.api.domain.community.post.PostRepository;
import com.mapshot.api.domain.notice.NoticeService;
import com.mapshot.api.domain.notice.NoticeType;
import com.mapshot.api.infra.encrypt.EncryptUtil;
import com.mapshot.api.infra.exception.ApiException;
import com.mapshot.api.infra.exception.status.ErrorCode;
import com.mapshot.api.presentation.admin.model.AdminRequest;
import com.mapshot.api.presentation.notice.model.NoticeRegistrationRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class AdminServiceTest {

    @Autowired
    private AdminService adminService;

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PostRepository postRepository;


    @BeforeEach
    void init() {
        adminRepository.save(AdminEntity.builder()
                .nickname("test")
                .password(EncryptUtil.encrypt("1234"))
                .build());
    }

    @AfterEach
    void release() {
        adminRepository.deleteAll();
    }


    @Test
    void 로그인_테스트() {
        AdminRequest request = AdminRequest.builder()
                .nickname("test")
                .password("1234")
                .build();

        assertThatNoException().isThrownBy(() -> adminService.validateUser(request));
    }

    @Test
    void 존재하지_않는_유저로_로그인시_예외_발생() {
        AdminRequest request = AdminRequest.builder()
                .nickname("i am not exist")
                .password("1234")
                .build();

        assertThatThrownBy(() -> adminService.validateUser(request))
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

        assertThatNoException().isThrownBy(() -> adminService.deletePost(id));
    }


    @Test
    void 공지사항_저장_테스트() {

        NoticeRegistrationRequest request = NoticeRegistrationRequest.builder()
                .noticeType(NoticeType.FIX.toString())
                .title("헬로")
                .content("방가방가")
                .build();

        long id = adminService.saveNotice(request);

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

        long id = adminService.saveNotice(request);

        long updatedId = adminService.modifyNotice(id,
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

        long id = adminService.saveNotice(request);

        assertThatThrownBy(() ->
                adminService.modifyNotice(id + 1,
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

        long id = adminService.saveNotice(request);

        assertThatNoException()
                .isThrownBy(() -> adminService.deleteNotice(id));

    }


    @Test
    void 없는_데이터_삭제_요청시_예외_발생() {

        NoticeRegistrationRequest request = NoticeRegistrationRequest.builder()
                .noticeType(NoticeType.UPDATE.toString())
                .title("초기화")
                .content("초기화")
                .build();

        long id = adminService.saveNotice(request);

        assertThatThrownBy(() -> adminService.deleteNotice(id + 1))
                .isInstanceOf(ApiException.class)
                .hasMessage(ErrorCode.NO_SUCH_NOTICE.getMessage());
    }


}
