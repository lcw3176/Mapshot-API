package com.mapshot.api.domain.community.post;


import com.mapshot.api.infra.exception.ApiException;
import com.mapshot.api.infra.exception.status.ErrorCode;
import com.mapshot.api.presentation.community.post.model.PostListResponse;
import com.mapshot.api.presentation.community.post.model.PostRegisterRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void init() {
        for (int i = 1; i < 100; i++) {
            String value = Integer.toString(i);

            postRepository.save(PostEntity.builder()
                    .title(value)
                    .content(value)
                    .writer(value)
                    .commentCount(i)
                    .password(value)
                    .build());
        }

    }

    @AfterEach
    void release() {
        postRepository.deleteAll();
    }

    @Test
    void id값_0으로_목록_조회시_가장_최신_게시글_10개_반환() {
        long id = 0;

        List<PostListResponse> responses = postService.getPostListById(id);

        assertEquals(responses.size(), 10);

        long recentId = postRepository.findFirstByOrderByIdDesc().getId();

        for (PostListResponse i : responses) {
            assertEquals(i.getId(), recentId);
            recentId--;
        }
    }


    @Test
    void 게시글_전체_목록_갯수보다_큰_id값으로_목록을_조회해도_가장_최신_게시글_10개_반환() {
        long id = postRepository.findFirstByOrderByIdDesc().getId() + 100;

        List<PostListResponse> responses = postService.getPostListById(id);

        assertEquals(responses.size(), 10);

        long recentId = postRepository.findFirstByOrderByIdDesc().getId();

        for (PostListResponse i : responses) {
            assertEquals(i.getId(), recentId);
            recentId--;
        }
    }


    @Test
    void _0미만의_id값으로_목록을_조회해도_가장_최신_게시글_10개_반환() {
        long id = -100;

        List<PostListResponse> responses = postService.getPostListById(id);

        assertEquals(responses.size(), 10);

        long recentId = postRepository.findFirstByOrderByIdDesc().getId();

        for (PostListResponse i : responses) {
            assertEquals(i.getId(), recentId);
            recentId--;
        }
    }

    @Test
    void 단일_게시글_조회() {
        long id = postRepository.findFirstByOrderByIdDesc().getId();

        assertThatNoException()
                .isThrownBy(() -> postService.getSinglePostById(id));
    }

    @Test
    void 없는_단일_게시글_조회시_예외_발생() {
        long id = -100;

        assertThatThrownBy(() -> postService.getSinglePostById(id))
                .isInstanceOf(ApiException.class)
                .hasMessageStartingWith(ErrorCode.NO_SUCH_POST.getMessage());

    }

    @Test
    void 게시글_저장() {
        assertThatNoException()
                .isThrownBy(() -> postService.save(
                        PostRegisterRequest.builder()
                                .content("hello")
                                .password("1234")
                                .title("good")
                                .writer("guest")
                                .build()));

    }

    @Test
    void 저장된_게시글의_비밀번호는_암호화됨() {
        String password = "1234";

        long id = postService.save(
                PostRegisterRequest.builder()
                        .content("hello")
                        .password(password)
                        .title("good")
                        .writer("guest")
                        .build());

        PostEntity entity = postRepository.findById(id).get();

        assertNotEquals(entity.getPassword(), password);
    }

    @Test
    void 생성된_게시글의_댓글_갯수는_0임() {
        String password = "1234";

        long id = postService.save(
                PostRegisterRequest.builder()
                        .content("hello")
                        .password(password)
                        .title("good")
                        .writer("guest")
                        .build());

        PostEntity entity = postRepository.findById(id).get();

        assertEquals(entity.getCommentCount(), 0);
    }
}