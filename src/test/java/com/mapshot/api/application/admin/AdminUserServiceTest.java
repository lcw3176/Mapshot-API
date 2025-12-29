package com.mapshot.api.application.admin;

import com.mapshot.api.domain.admin.AdminUser;
import com.mapshot.api.domain.admin.AdminUserRepository;
import com.mapshot.api.domain.notice.Notice;
import com.mapshot.api.domain.notice.NoticeRepository;
import com.mapshot.api.domain.notice.NoticeType;
import com.mapshot.api.infra.exception.ApiException;
import com.mapshot.api.infra.exception.status.ErrorCode;
import com.mapshot.api.infra.util.EncryptUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AdminUserServiceTest {

    @Autowired
    private AdminUserService adminUserService;

    @Autowired
    private AdminUserRepository adminUserRepository;

    @Autowired
    private NoticeRepository noticeRepository;

    private AdminUser testAdminUser;
    private String testPassword;

    @BeforeEach
    void setUp() {
        testPassword = "testPassword123";
        String encryptedPassword = EncryptUtil.encrypt(testPassword);
        
        testAdminUser = AdminUser.builder()
                .userName("testUser")
                .password(encryptedPassword)
                .build();
        adminUserRepository.save(testAdminUser);
    }

    @AfterEach
    void tearDown() {
        noticeRepository.deleteAll();
        adminUserRepository.deleteAll();
    }

    @Test
    void 로그인_성공() {
        // when & then
        assertDoesNotThrow(() -> adminUserService.login("testUser", testPassword));
    }

    @Test
    void 로그인_실패_존재하지_않는_유저() {
        // when & then
        assertThatThrownBy(() -> adminUserService.login("nonExistentUser", testPassword))
                .isInstanceOf(ApiException.class)
                .hasMessage(ErrorCode.NO_SUCH_USER.getMessage());
    }

    @Test
    void 로그인_실패_잘못된_비밀번호() {
        // when & then
        assertThatThrownBy(() -> adminUserService.login("testUser", "wrongPassword"))
                .isInstanceOf(ApiException.class)
                .hasMessage(ErrorCode.NO_SUCH_USER.getMessage());
    }

    @Test
    void 공지사항_저장_성공() {
        // given
        NoticeType type = NoticeType.UPDATE;
        String title = "테스트 제목";
        String content = "테스트 내용";

        // when
        long noticeId = adminUserService.saveNotice(type, title, content);

        // then
        assertThat(noticeId).isPositive();
        Notice savedNotice = noticeRepository.findById(noticeId).orElseThrow();
        assertThat(savedNotice.getTitle()).isEqualTo(title);
        assertThat(savedNotice.getContent()).isEqualTo(content);
        assertThat(savedNotice.getNoticeType()).isEqualTo(type);
    }

    @Test
    void 공지사항_삭제_성공() {
        // given
        Notice notice = Notice.builder()
                .noticeType(NoticeType.UPDATE)
                .title("삭제할 공지사항")
                .content("내용")
                .build();
        long noticeId = noticeRepository.save(notice).getId();

        // when
        adminUserService.deleteNotice(noticeId);

        // then
        assertThat(noticeRepository.findById(noticeId)).isEmpty();
    }

    @Test
    void 공지사항_삭제_실패_존재하지_않는_공지사항() {
        // when & then
        assertThatThrownBy(() -> adminUserService.deleteNotice(-1L))
                .isInstanceOf(ApiException.class)
                .hasMessage(ErrorCode.NO_SUCH_NOTICE.getMessage());
    }

    @Test
    void 공지사항_수정_성공() {
        // given
        Notice notice = Notice.builder()
                .noticeType(NoticeType.UPDATE)
                .title("원래 제목")
                .content("원래 내용")
                .build();
        long noticeId = noticeRepository.save(notice).getId();

        NoticeType newType = NoticeType.FIX;
        String newTitle = "수정된 제목";
        String newContent = "수정된 내용";

        // when
        long modifiedId = adminUserService.modifyNotice(noticeId, newType, newTitle, newContent);

        // then
        assertThat(modifiedId).isEqualTo(noticeId);
        Notice modifiedNotice = noticeRepository.findById(noticeId).orElseThrow();
        assertThat(modifiedNotice.getTitle()).isEqualTo(newTitle);
        assertThat(modifiedNotice.getContent()).isEqualTo(newContent);
        assertThat(modifiedNotice.getNoticeType()).isEqualTo(newType);
    }

    @Test
    void 공지사항_수정_실패_존재하지_않는_공지사항() {
        // when & then
        assertThatThrownBy(() -> adminUserService.modifyNotice(-1L, NoticeType.UPDATE, "제목", "내용"))
                .isInstanceOf(ApiException.class)
                .hasMessage(ErrorCode.NO_SUCH_NOTICE.getMessage());
    }

    @Test
    void 공지사항_수정_모든_타입_테스트() {
        // given
        Notice notice = Notice.builder()
                .noticeType(NoticeType.UPDATE)
                .title("제목")
                .content("내용")
                .build();
        long noticeId = noticeRepository.save(notice).getId();

        // when & then - UPDATE
        adminUserService.modifyNotice(noticeId, NoticeType.UPDATE, "제목1", "내용1");
        Notice updated = noticeRepository.findById(noticeId).orElseThrow();
        assertThat(updated.getNoticeType()).isEqualTo(NoticeType.UPDATE);

        // when & then - FIX
        adminUserService.modifyNotice(noticeId, NoticeType.FIX, "제목2", "내용2");
        updated = noticeRepository.findById(noticeId).orElseThrow();
        assertThat(updated.getNoticeType()).isEqualTo(NoticeType.FIX);

        // when & then - RESERVED_CHECK
        adminUserService.modifyNotice(noticeId, NoticeType.RESERVED_CHECK, "제목3", "내용3");
        updated = noticeRepository.findById(noticeId).orElseThrow();
        assertThat(updated.getNoticeType()).isEqualTo(NoticeType.RESERVED_CHECK);
    }
}

