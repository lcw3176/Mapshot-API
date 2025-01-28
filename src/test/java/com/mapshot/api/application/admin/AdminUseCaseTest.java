package com.mapshot.api.application.admin;

import com.mapshot.api.domain.admin.user.AdminUserEntity;
import com.mapshot.api.domain.admin.user.AdminUserRepository;
import com.mapshot.api.domain.community.comment.CommentEntity;
import com.mapshot.api.domain.community.comment.CommentRepository;
import com.mapshot.api.domain.community.comment.CommentService;
import com.mapshot.api.domain.community.post.PostRepository;
import com.mapshot.api.domain.community.post.PostService;
import com.mapshot.api.domain.notice.NoticeEntity;
import com.mapshot.api.domain.notice.NoticeRepository;
import com.mapshot.api.domain.notice.NoticeService;
import com.mapshot.api.domain.notice.NoticeType;
import com.mapshot.api.infra.util.EncryptUtil;
import com.mapshot.api.infra.exception.ApiException;
import com.mapshot.api.infra.exception.status.ErrorCode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class AdminUseCaseTest {

    @Autowired
    private AdminUseCase adminUseCase;

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private NoticeService noticeService;


    @Autowired
    private AdminUserRepository adminUserRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private NoticeRepository noticeRepository;


    @BeforeEach
    void init() {
        for (int i = 1; i < 10; i++) {

            String value = Integer.toString(i);

            long postId = postService.save(value, value, value, value);

            for (int j = 0; j < 100; j++) {
                commentService.save(value, value, postId, value);
            }
        }

        adminUserRepository.save(
                AdminUserEntity.builder()
                        .nickname("hello")
                        .password(EncryptUtil.encrypt("1234"))
                        .build());
    }

    @AfterEach
    void release() {
        adminUserRepository.deleteAll();
        postRepository.deleteAll();
        commentRepository.deleteAll();
        noticeRepository.deleteAll();
    }


    @Test
    void 관리자_권한으로_게시글_삭제_테스트() {
        long postId = postService.getPostsByPageNumber(0).getContent().get(0).getId();

        adminUseCase.deletePost(postId);

        assertThatThrownBy(() -> postService.getPostById(postId))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining(ErrorCode.NO_SUCH_POST.getMessage());

        Page<CommentEntity> comments = commentService.findAllByPostId(0, postId);

        assertEquals(comments.getTotalPages(), 0);
        assertEquals(comments.getContent().size(), 0);
    }

    @Test
    void 관리자_로그인() {
        assertThatNoException()
                .isThrownBy(() -> adminUseCase.login("hello", "1234"));
    }

    @Test
    void 관리자_공지사항_등록() {
        adminUseCase.saveNotice(NoticeType.UPDATE, "new_title", "new_content");

        NoticeEntity notice = noticeRepository.findFirstByOrderByIdDesc();

        assertEquals(notice.getNoticeType(), NoticeType.UPDATE);
        assertEquals(notice.getTitle(), "new_title");
        assertEquals(notice.getContent(), "new_content");
    }

    @Test
    void 관리자_공지사항_수정() {
        NoticeEntity notice = noticeRepository.save(NoticeEntity.builder()
                .title("title")
                .noticeType(NoticeType.RESERVED_CHECK)
                .content("content")
                .build());

        adminUseCase.modifyNotice(notice.getId(), NoticeType.FIX, "fixed_title", "fixed_content");

        notice = noticeRepository.findFirstByOrderByIdDesc();


        assertEquals(notice.getNoticeType(), NoticeType.FIX);
        assertEquals(notice.getTitle(), "fixed_title");
        assertEquals(notice.getContent(), "fixed_content");
    }

    @Test
    void 관리자_공지사항_삭제() {
        long noticeId = noticeRepository.save(NoticeEntity.builder()
                .title("title")
                .noticeType(NoticeType.RESERVED_CHECK)
                .content("content")
                .build()).getId();

        adminUseCase.deleteNotice(noticeId);

        assertThatThrownBy(() -> noticeService.findById(noticeId))
                .isInstanceOf(ApiException.class)
                .hasMessage(ErrorCode.NO_SUCH_NOTICE.getMessage());
    }

    @Test
    void 관리자_게시글_삭제() {
        long postId = postRepository.findFirstByOrderByIdDesc().getId();

        adminUseCase.deletePost(postId);

        assertThatThrownBy(() -> postService.getPostById(postId))
                .isInstanceOf(ApiException.class)
                .hasMessage(ErrorCode.NO_SUCH_POST.getMessage());
    }
}