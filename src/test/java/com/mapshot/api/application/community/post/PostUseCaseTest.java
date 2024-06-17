package com.mapshot.api.application.community.post;

import com.mapshot.api.domain.community.comment.CommentEntity;
import com.mapshot.api.domain.community.comment.CommentRepository;
import com.mapshot.api.domain.community.post.PostEntity;
import com.mapshot.api.domain.community.post.PostRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
    void 게시글_목록을_가져온다() {
        PostListResponse response = postUseCase.getPostList(1);

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
    void 게시글을_삭제한다() {
        postUseCase.save("writer", "content", "title", "password");

        PostEntity post = postRepository.findFirstByOrderByIdDesc();

        assertDoesNotThrow(() -> postUseCase.deleteIfOwner(post.getId(), "password"));
    }
}