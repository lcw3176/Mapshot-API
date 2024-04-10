package com.mapshot.api.domain.community.comment;

import com.mapshot.api.domain.community.post.PostEntity;
import com.mapshot.api.domain.community.post.PostRepository;
import com.mapshot.api.infra.exception.ApiException;
import com.mapshot.api.infra.exception.status.ErrorCode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
class CommentServiceTest {


    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Value("${community.comment.page_size}")
    private Integer size;

    @BeforeEach
    void init() {
        for (int i = 1; i < 10; i++) {

            String value = Integer.toString(i);

            PostEntity post = postRepository.save(PostEntity.builder()
                    .title(value)
                    .content(value)
                    .writer(value)
                    .commentCount(i)
                    .password(value)
                    .build());


            for (int j = 0; j < 100; j++) {
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
    void release() {
        postRepository.deleteAll();
        commentRepository.deleteAll();
    }

    @Test
    void page값_0으로_목록_조회시_가장_최신_댓글_20개_반환() {
        long id = postRepository.findFirstByOrderByIdDesc().getId();

        CommentResponse responses = commentService.getComments(0, id);

        assertEquals(responses.getComments().size(), size);
    }

    @Test
    void page값_1으로_목록_조회시_가장_최신_댓글_20개_반환() {
        long id = postRepository.findFirstByOrderByIdDesc().getId();
        int pageNumber = 1;

        CommentResponse responses = commentService.getComments(pageNumber, id);

        assertEquals(responses.getComments().size(), size);
    }


    @Test
    void 댓글_전체_목록_갯수보다_큰_page값으로_목록을_조회하면_아무것도_반환하지_않음() {
        int page = 1000000;
        long postId = postRepository.findFirstByOrderByIdDesc().getId();

        CommentResponse responses = commentService.getComments(page, postId);

        assertEquals(responses.getComments().size(), 0);

    }


    @Test
    void _0미만의_page값으로_목록을_조회해도_가장_최신_댓글_20개_반환() {
        int page = -100;
        long postId = postRepository.findFirstByOrderByIdDesc().getId();

        CommentResponse responses = commentService.getComments(page, postId);

        assertEquals(responses.getComments().size(), size);
    }

    @Test
    void 댓글_조회() {
        long id = postRepository.findFirstByOrderByIdDesc().getId();

        assertThatNoException()
                .isThrownBy(() -> commentService.getComments(0, id));
    }


    @Test
    void 댓글_저장() {
        long id = postRepository.findFirstByOrderByIdDesc().getId();

        assertThatNoException()
                .isThrownBy(() -> commentService.save("guest", "hello", id, "123"));

    }

    @Test
    void 저장된_댓글의_비밀번호는_암호화됨() {
        String password = "1234";
        long postId = postRepository.findFirstByOrderByIdDesc().getId();

        long id = commentService.save("guest", "hello", postId, password);

        CommentEntity entity = commentRepository.findById(id).get();

        assertNotEquals(entity.getPassword(), password);
    }


    @Test
    void 댓글_삭제() {
        String password = "1234";
        long postId = postRepository.findFirstByOrderByIdDesc().getId();

        long id = commentService.save("guest", "hello", postId, password);

        assertThatNoException().isThrownBy(() -> commentService.deleteIfOwner(id, password));
    }

    @Test
    void 잘못된_비밀번호_입력시_예외_발생() {
        String password = "1234";
        long postId = postRepository.findFirstByOrderByIdDesc().getId();

        long id = commentService.save("guest", "hello", postId, password);

        assertThatThrownBy(() -> commentService.deleteIfOwner(id, "hello"))
                .isInstanceOf(ApiException.class)
                .hasMessageStartingWith(ErrorCode.NOT_VALID_PASSWORD.getMessage());
    }


    @Test
    void 존재하지_않는_댓글_삭제_시도시_예외() {
        assertThatThrownBy(() -> commentService.deleteIfOwner(-100, "hello"))
                .isInstanceOf(ApiException.class)
                .hasMessageStartingWith(ErrorCode.NO_SUCH_POST.getMessage());
    }
}