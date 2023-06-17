package com.mapshot.api.admin.service;

import com.mapshot.api.admin.model.AdminRequest;
import com.mapshot.api.common.exception.ApiException;
import com.mapshot.api.common.exception.status.ErrorCode;
import com.mapshot.api.common.token.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.MultiValueMap;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class AdminServiceTest {

    @Autowired
    private AdminService adminService;

    @Test
    void 토큰_생성_테스트() {
        MultiValueMap<String, String> headers = adminService.makeToken();
        String token = headers.getFirst(JwtUtil.ADMIN_HEADER_NAME);

        assertTrue(JwtUtil.isValidAdmin(token));
    }


    @Test
    void 로그인_테스트() {
        AdminRequest request = AdminRequest.builder()
                .nickname("test")
                .password("1234")
                .build();

        MultiValueMap<String, String> headers = adminService.login(request);
        String token = headers.getFirst(JwtUtil.ADMIN_HEADER_NAME);

        assertTrue(JwtUtil.isValidAdmin(token));
    }

    @Test
    void 존재하지_않는_유저로_로그인시_예외_발생() {
        AdminRequest request = AdminRequest.builder()
                .nickname("i am not exist")
                .password("1234")
                .build();

        assertThatThrownBy(() -> adminService.login(request))
                .isInstanceOf(ApiException.class)
                .hasMessage(ErrorCode.NO_SUCH_USER.getMessage());
    }
    
}