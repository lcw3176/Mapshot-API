package com.mapshot.api.domain.admin.community.post;

import com.mapshot.api.domain.community.post.PostEntity;
import com.mapshot.api.infra.encrypt.EncryptUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThatNoException;

@SpringBootTest
class AdminPostServiceTest {

    @Autowired
    private AdminPostRepository adminPostRepository;

    @Autowired
    private AdminPostService adminPostService;


    @Test
    void 게시글_관리자_권한으로_삭제() {
        long id = adminPostRepository.save(PostEntity.builder()
                .title("hello")
                .writer("good")
                .content("hello")
                .commentCount(0)
                .password(EncryptUtil.encrypt("1234"))
                .build()).getId();

        assertThatNoException().isThrownBy(() -> adminPostService.deletePost(id));
    }

}