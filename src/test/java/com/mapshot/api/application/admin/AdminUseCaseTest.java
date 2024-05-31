package com.mapshot.api.application.admin;

import com.mapshot.api.domain.community.comment.CommentEntity;
import com.mapshot.api.domain.community.comment.CommentService;
import com.mapshot.api.domain.community.post.PostService;
import com.mapshot.api.infra.exception.ApiException;
import com.mapshot.api.infra.exception.status.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

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

    @BeforeEach
    void init() {
        for (int i = 1; i < 10; i++) {

            String value = Integer.toString(i);

            long postId = postService.save(value, value, value, value);
            
            for (int j = 0; j < 100; j++) {
                commentService.save(value, value, postId, value);
            }
        }
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
}