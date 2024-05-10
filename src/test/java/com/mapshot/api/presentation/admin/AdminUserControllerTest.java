package com.mapshot.api.presentation.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapshot.api.SlackMockExtension;
import com.mapshot.api.domain.admin.user.AdminUserEntity;
import com.mapshot.api.domain.admin.user.AdminUserRepository;
import com.mapshot.api.infra.auth.Validation;
import com.mapshot.api.infra.encrypt.EncryptUtil;
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
class AdminUserControllerTest extends SlackMockExtension {


    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Validation adminValidation;

    @Autowired
    private AdminUserRepository adminUserRepository;

    @Value("${jwt.admin.header}")
    private String ADMIN_HEADER_NAME;

    private static final String BASE_URL = "/admin";

    @BeforeEach
    void init() {
        adminUserRepository.save(AdminUserEntity.builder()
                .nickname("test")
                .password(EncryptUtil.encrypt("1234"))
                .build());
    }


    @Test
    void 관리자_로그인_테스트() throws Exception {
        AdminUserRequest request = AdminUserRequest
                .builder()
                .nickname("test")
                .password("1234")
                .build();

        mockMvc.perform(
                        RestDocumentationRequestBuilders.post(BASE_URL + "/user/login")
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

    @Test
    void 관리자_로그인_연장_테스트() throws Exception {
        mockMvc.perform(
                        RestDocumentationRequestBuilders.post(BASE_URL + "/user/auth/refresh")
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
