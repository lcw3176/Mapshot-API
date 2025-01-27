package com.mapshot.api.domain.community.post;


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
    void page값_0으로_목록_조회시_가장_최신_게시글_10개_반환() {
        int id = 0;

        Page<PostEntity> responses = postService.getPostsByPageNumber(id);

        assertEquals(responses.getContent().size(), 10);

        long recentId = postRepository.findFirstByOrderByIdDesc().getId();

        for (PostEntity i : responses.getContent()) {
            assertEquals(i.getId(), recentId);
            recentId--;
        }
    }

    @Test
    void page값_1으로_목록_조회시_가장_최신_게시글_10개_반환() {
        int id = 1;

        Page<PostEntity> responses = postService.getPostsByPageNumber(id);

        assertEquals(responses.getContent().size(), 10);

        long recentId = postRepository.findFirstByOrderByIdDesc().getId();

        for (PostEntity i : responses.getContent()) {
            assertEquals(i.getId(), recentId);
            recentId--;
        }
    }


    @Test
    void 게시글_전체_목록_갯수보다_큰_page값으로_목록을_조회하면_아무것도_반환하지_않음() {
        long page = postRepository.findFirstByOrderByIdDesc().getId() + 100;

        Page<PostEntity> responses = postService.getPostsByPageNumber((int) page);

        assertEquals(responses.getContent().size(), 0);

    }


    @Test
    void _0미만의_pag값으로_목록을_조회해도_가장_최신_게시글_10개_반환() {
        int page = -100;

        Page<PostEntity> responses = postService.getPostsByPageNumber(page);

        assertEquals(responses.getContent().size(), 10);

        long recentId = postRepository.findFirstByOrderByIdDesc().getId();

        for (PostEntity i : responses.getContent()) {
            assertEquals(i.getId(), recentId);
            recentId--;
        }
    }

    @Test
    void 단일_게시글_조회() {
        long id = postRepository.findFirstByOrderByIdDesc().getId();

        assertThatNoException()
                .isThrownBy(() -> postService.getPostById(id));
    }

    @Test
    void 없는_단일_게시글_조회시_예외_발생() {
        long id = -100;

        assertThatThrownBy(() -> postService.getPostById(id))
                .isInstanceOf(ApiException.class)
                .hasMessageStartingWith(ErrorCode.NO_SUCH_POST.getMessage());

    }

    @Test
    void 게시글_저장() {
        assertThatNoException()
                .isThrownBy(() -> postService.save("guest", "hello", "hello", "123"));

    }

    @Test
    void 저장된_게시글의_비밀번호는_암호화됨() {
        String password = "1234";

        long id = postService.save("guest", "hello", "hello", password);

        PostEntity entity = postRepository.findById(id).get();

        assertNotEquals(entity.getPassword(), password);
    }

    @Test
    void 생성된_게시글의_댓글_갯수는_0임() {
        String password = "1234";

        long id = postService.save("guest", "hello", "hello", password);

        PostEntity entity = postRepository.findById(id).get();

        assertEquals(entity.getCommentCount(), 0);
    }


    @Test
    void 게시글_삭제() {
        String password = "1234";

        long id = postService.save("guest", "hello", "hello", password);

        assertThatNoException().isThrownBy(() -> postService.deleteIfOwner(id, password));
    }

    @Test
    void 잘못된_비밀번호_입력시_예외_발생() {
        String password = "1234";

        long id = postService.save("guest", "hello", "hello", password);

        assertThatThrownBy(() -> postService.deleteIfOwner(id, "hello"))
                .isInstanceOf(ApiException.class)
                .hasMessageStartingWith(ErrorCode.NOT_VALID_PASSWORD.getMessage());
    }


    @Test
    void 존재하지_않는_게시글_삭제_시도시_예외() {
        assertThatThrownBy(() -> postService.deleteIfOwner(-100, "hello"))
                .isInstanceOf(ApiException.class)
                .hasMessageStartingWith(ErrorCode.NO_SUCH_POST.getMessage());
    }

    @Test
    void 게시글_관리자_권한_삭제() {
        long id = postRepository.save(PostEntity.builder()
                .title("hello")
                .writer("good")
                .content("hello")
                .commentCount(0)
                .password(EncryptUtil.encrypt("1234"))
                .build()).getId();

        assertThatNoException().isThrownBy(() -> postService.deleteById(id));
    }

}