package com.mapshot.api.application.community.post;

import com.mapshot.api.domain.community.comment.CommentEntity;
import com.mapshot.api.domain.community.comment.CommentRepository;
import com.mapshot.api.domain.community.post.PostEntity;
import com.mapshot.api.domain.community.post.PostRepository;
import com.mapshot.api.infra.exception.ApiException;
import com.mapshot.api.infra.exception.status.ErrorCode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
class PostUseCaseTest {

    @Autowired
    private PostUseCase postUseCase;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @BeforeEach
    public void afterEach() {
        int commentCount = 100;
        int postCount = 10;

        for (int i = 0; i < postCount; i++) {

            String value = Integer.toString(i);

            PostEntity post = postRepository.save(PostEntity.builder()
                    .title(value)
                    .content(value)
                    .writer(value)
                    .commentCount(commentCount)
                    .password(value)
                    .build());


            for (int j = 0; j < commentCount; j++) {
                commentRepository.save(CommentEntity.builder()
                        .content(value)
                        .writer(value)
                        .deleted(false)
                        .password(value)
                        .postId(post.getId())
                        .build());
            }
        }
    }

    @AfterEach
    public void beforeEach() {
        postRepository.deleteAll();
        commentRepository.deleteAll();
    }

    @Test
    void 게시글을_가져온다() {
        PostEntity post = postRepository.findFirstByOrderByIdDesc();
        List<CommentEntity> comments = commentRepository.findAllByPostIdAndDeletedFalse(post.getId());

        PostDetailResponse response = postUseCase.getPost(post.getId());

        assertEquals(post.getId(), response.getId());
        assertEquals(post.getWriter(), response.getWriter());
        assertEquals(post.getTitle(), response.getTitle());
        assertEquals(post.getContent(), response.getContent());
        assertEquals(post.getCommentCount(), comments.size());
        assertEquals(post.getCreatedDate(), response.getCreatedDate());
    }

    @Test
    void 존재하지_않는_게시글을_가져오면_예와가_발생한다() {
        assertThatThrownBy(() -> postUseCase.getPost(-1))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining(ErrorCode.NO_SUCH_POST.getMessage());
    }

    @Test
    void 게시글_목록을_가져온다() {
        PostListResponse response = postUseCase.getPostList(1);

        assertEquals(response.getTotalPage(), 1);
        assertEquals(response.getPosts().size(), 10);
    }

    @Test
    void _0이하의_페이지_조회시_첫번째_페이지를_반환한다() {
        PostListResponse response = postUseCase.getPostList(-1);

        assertEquals(response.getTotalPage(), 1);
        assertEquals(response.getPosts().size(), 10);
    }
    

    @Test
    void 게시글을_저장한다() {
        postUseCase.save("writer", "content", "title", "password");

        PostEntity post = postRepository.findFirstByOrderByIdDesc();

        assertEquals(post.getWriter(), "writer");
        assertEquals(post.getContent(), "content");
        assertEquals(post.getTitle(), "title");
    }

    @Test
    void 비밀번호는_저장시_암호화된다() {
        postUseCase.save("writer", "content", "title", "password");

        PostEntity post = postRepository.findFirstByOrderByIdDesc();

        assertNotEquals(post.getPassword(), "password");
    }


    @Test
    void 게시글을_삭제한다() {
        postUseCase.save("writer", "content", "title", "password");

        PostEntity post = postRepository.findFirstByOrderByIdDesc();

        assertThatNoException().isThrownBy(() -> postUseCase.deleteIfOwner(post.getId(), "password"));
    }


    @Test
    void 작성자가_아니면_게시글을_삭제할수_없다() {
        PostEntity post = postRepository.findFirstByOrderByIdDesc();

        assertThatThrownBy(() -> postUseCase.deleteIfOwner(post.getId(), "wrong password"))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining(ErrorCode.NOT_VALID_PASSWORD.getMessage());
    }
}