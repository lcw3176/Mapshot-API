package com.mapshot.api.presentation.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapshot.api.SlackMockExtension;
import com.mapshot.api.domain.admin.AdminEntity;
import com.mapshot.api.domain.admin.AdminRepository;
import com.mapshot.api.infra.auth.Validation;
import com.mapshot.api.infra.exception.ApiException;
import com.mapshot.api.infra.exception.status.ErrorCode;
import com.mapshot.api.presentation.admin.model.AdminRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class AdminControllerTest extends SlackMockExtension {


    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Validation adminValidation;

    @Autowired
    private AdminRepository adminRepository;

    @Value("${jwt.admin.header}")
    private String ADMIN_HEADER_NAME;


    private static final String ENCRYPT_ALGORITHM = "SHA-256";
    private static final String BASE_URL = "/admin";

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
    void 관리자_로그인_테스트() throws Exception {
        AdminRequest request = AdminRequest
                .builder()
                .nickname("test")
                .password("1234")
                .build();

        mockMvc.perform(
                        RestDocumentationRequestBuilders.post(BASE_URL + "/login")
                                .content(mapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("admin/login",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("nickname").description("관리자 아이디"),
                                fieldWithPath("password").description("관리자 비밀번호")
                        )));
    }

//    @Test
//    void 관리자_로그아웃_테스트() throws Exception {
//
//
//        mockMvc.perform(
//                        RestDocumentationRequestBuilders.post(BASE_URL + "/logout")
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .header(ADMIN_HEADER_NAME, adminToken.generate()))
//                .andExpect(status().isOk())
//                .andDo(document("admin/logout",
//                        preprocessRequest(prettyPrint()),
//                        preprocessResponse(prettyPrint()),
//                        requestHeaders(
//                                headerWithName(ADMIN_HEADER_NAME).description("관리자 인증 토큰")
//                        )));
//    }

//    @Test
//    void 토큰없이_관리자_로그아웃_요청시_예외() throws Exception {
//
//
//        mockMvc.perform(
//                        post(BASE_URL + "/logout")
//                                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().is4xxClientError());
//    }


    @Test
    void 관리자_로그인_연장_테스트() throws Exception {
        mockMvc.perform(
                        RestDocumentationRequestBuilders.post(BASE_URL + "/auth/refresh")
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(HttpHeaders.readOnlyHttpHeaders(adminValidation.makeHeader())))
                .andExpect(status().isOk())
                .andDo(document("admin/auth/refresh",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName(ADMIN_HEADER_NAME).description("관리자 인증 토큰")
                        )));
    }

    @Test
    void 토큰_없이_관리자_로그인_연장_요청시_예외() throws Exception {
        mockMvc.perform(
                        post(BASE_URL + "/auth/refresh")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }


}
