package com.mapshot.api.domain.admin;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AdminUserTest {

    @Test
    void 관리자_유저_생성_성공() {
        // given
        String userName = "testUser";
        String password = "testPassword123";

        // when
        AdminUser adminUser = AdminUser.builder()
                .userName(userName)
                .password(password)
                .build();

        // then
        assertThat(adminUser.getUserName()).isEqualTo(userName);
        assertThat(adminUser.getPassword()).isEqualTo(password);
    }

    @Test
    void 관리자_유저_빌더_패턴_테스트() {
        // when
        AdminUser adminUser = AdminUser.builder()
                .userName("admin")
                .password("password")
                .build();

        // then
        assertThat(adminUser).isNotNull();
        assertThat(adminUser.getUserName()).isEqualTo("admin");
        assertThat(adminUser.getPassword()).isEqualTo("password");
    }
}

