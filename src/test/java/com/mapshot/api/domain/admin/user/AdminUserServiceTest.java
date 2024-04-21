package com.mapshot.api.domain.admin.user;

import com.mapshot.api.infra.encrypt.EncryptUtil;
import com.mapshot.api.infra.exception.ApiException;
import com.mapshot.api.infra.exception.status.ErrorCode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class AdminUserServiceTest {

    @Autowired
    private AdminUserService adminUserService;

    @Autowired
    private AdminUserRepository adminUserRepository;
    
    @BeforeEach
    void init() {
        adminUserRepository.save(AdminUserEntity.builder()
                .nickname("test")
                .password(EncryptUtil.encrypt("1234"))
                .build());
    }

    @AfterEach
    void release() {
        adminUserRepository.deleteAll();
    }


    @Test
    void 로그인_테스트() {
        assertThatNoException().isThrownBy(() -> adminUserService.validateUser("test", "1234"));
    }

    @Test
    void 존재하지_않는_유저로_로그인시_예외_발생() {
        assertThatThrownBy(() -> adminUserService.validateUser("i am not exist", "1234"))
                .isInstanceOf(ApiException.class)
                .hasMessage(ErrorCode.NO_SUCH_USER.getMessage());
    }


}
