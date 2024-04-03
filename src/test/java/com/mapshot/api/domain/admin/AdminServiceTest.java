package com.mapshot.api.domain.admin;

import com.mapshot.api.domain.community.post.PostEntity;
import com.mapshot.api.domain.community.post.PostRepository;
import com.mapshot.api.infra.encrypt.EncryptUtil;
import com.mapshot.api.infra.exception.ApiException;
import com.mapshot.api.infra.exception.status.ErrorCode;
import com.mapshot.api.presentation.admin.model.AdminRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class AdminServiceTest {

    @Autowired
    private AdminService adminService;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PostRepository postRepository;


    @BeforeEach
    void init() {
        adminRepository.save(AdminEntity.builder()
                .nickname("test")
                .password(EncryptUtil.encrypt("1234"))
                .build());
    }

    @AfterEach
    void release() {
        adminRepository.deleteAll();
    }


    @Test
    void 로그인_테스트() {
        AdminRequest request = AdminRequest.builder()
                .nickname("test")
                .password("1234")
                .build();

        assertThatNoException().isThrownBy(() -> adminService.validateUser(request));
    }

    @Test
    void 존재하지_않는_유저로_로그인시_예외_발생() {
        AdminRequest request = AdminRequest.builder()
                .nickname("i am not exist")
                .password("1234")
                .build();

        assertThatThrownBy(() -> adminService.validateUser(request))
                .isInstanceOf(ApiException.class)
                .hasMessage(ErrorCode.NO_SUCH_USER.getMessage());
    }


    @Test
    void 게시글_관리자_권한으로_삭제() {
        long id = postRepository.save(PostEntity.builder()
                .title("hello")
                .writer("good")
                .content("hello")
                .commentCount(0)
                .password(EncryptUtil.encrypt("1234"))
                .build()).getId();

        assertThatNoException().isThrownBy(() -> adminService.deletePost(id));
    }

}
