package com.mapshot.api.application.community.comment;

import com.mapshot.api.domain.community.comment.CommentEntity;
import com.mapshot.api.domain.community.comment.CommentRepository;
import com.mapshot.api.domain.community.post.PostEntity;
import com.mapshot.api.domain.community.post.PostRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class CommentUseCaseTest {

    @Autowired
    private CommentUseCase commentUseCase;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    public void afterEach() {
        int commentCount = 100;
        int postCount = 10;

        for (int i = 1; i < postCount; i++) {

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
    void 댓글을_가져온다() {
        long postId = postRepository.findFirstByOrderByIdDesc().getId();
        CommentResponse response = commentUseCase.getComments(1, postId);

        assertEquals(response.getTotalPage(), 5);
        assertEquals(response.getComments().size(), 20);
    }

    @Test
    void 댓글을_저장하면_게시글의_댓글_카운트가_올라간다() {
        long postId = postRepository.findFirstByOrderByIdDesc().getId();
        long commentId = commentUseCase.save("writer", "content", postId, "password");
        long commentCount = postRepository.findById(postId).get().getCommentCount();

        assertEquals(101, commentCount);
    }

    @Test
    void 댓글을_삭제하면_댓글_카운트가_감소한다() {
        long postId = postRepository.findFirstByOrderByIdDesc().getId();
        long commentId = commentUseCase.save("writer", "content", postId, "password");

        commentUseCase.deleteIfOwner(commentId, "password");
        long commentCount = postRepository.findById(postId).get().getCommentCount();

        assertEquals(100, commentCount);
    }
}