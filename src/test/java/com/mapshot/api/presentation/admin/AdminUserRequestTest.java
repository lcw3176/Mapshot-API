package com.mapshot.api.presentation.admin;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AdminUserRequestTest {

    @Test
    void AdminUserRequest_생성_성공() {
        // when
        AdminUserRequest request = AdminUserRequest.builder()
                .nickname("testUser")
                .password("testPassword")
                .build();

        // then
        assertThat(request.getNickname()).isEqualTo("testUser");
        assertThat(request.getPassword()).isEqualTo("testPassword");
    }

    @Test
    void AdminUserRequest_빌더_패턴_테스트() {
        // when
        AdminUserRequest request = AdminUserRequest.builder()
                .nickname("admin")
                .password("password123")
                .build();

        // then
        assertThat(request).isNotNull();
        assertThat(request.getNickname()).isEqualTo("admin");
        assertThat(request.getPassword()).isEqualTo("password123");
    }

    @Test
    void AdminUserRequest_기본_생성자_테스트() {
        // when
        AdminUserRequest request = new AdminUserRequest();

        // then
        assertThat(request).isNotNull();
    }

    @Test
    void AdminUserRequest_전체_생성자_테스트() {
        // when
        AdminUserRequest request = new AdminUserRequest("testUser", "testPassword");

        // then
        assertThat(request.getNickname()).isEqualTo("testUser");
        assertThat(request.getPassword()).isEqualTo("testPassword");
    }
}

