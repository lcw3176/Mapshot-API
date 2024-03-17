package com.mapshot.api.admin.service;

import com.mapshot.api.admin.entity.AdminEntity;
import com.mapshot.api.admin.model.AdminRequest;
import com.mapshot.api.admin.repository.AdminRepository;
import com.mapshot.api.infra.web.auth.Validation;
import com.mapshot.api.infra.web.exception.ApiException;
import com.mapshot.api.infra.web.exception.status.ErrorCode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class AdminServiceTest {

    @Autowired
    private AdminService adminService;

    @Autowired
    private AdminRepository adminRepository;


    @Autowired
    private Validation adminValidation;
    private static final String ENCRYPT_ALGORITHM = "SHA-256";

    @BeforeEach
    void init() {
        adminRepository.save(AdminEntity.builder()
                .nickname("test")
                .password(encrypt("1234"))
                .build());
    }

    @AfterEach
    void release() {
        adminRepository.deleteAll();
    }


    private String encrypt(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance(ENCRYPT_ALGORITHM);
            md.update(text.getBytes());

            return bytesToHex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new ApiException(ErrorCode.NO_SUCH_ALGORITHM);
        }

    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }


    @Test
    void 로그인_테스트() {
        AdminRequest request = AdminRequest.builder()
                .nickname("test")
                .password("1234")
                .build();

        adminService.validateUser(request);
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

}
