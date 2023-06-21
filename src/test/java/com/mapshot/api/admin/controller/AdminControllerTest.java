package com.mapshot.api.admin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapshot.api.admin.model.AdminRequest;
import com.mapshot.api.common.validation.token.AdminToken;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
class AdminControllerTest {

    @Autowired
    ObjectMapper mapper;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AdminToken adminToken;

    private static final String BASE_URL = "/admin";


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
//                                .header(adminToken.getHeaderName(), adminToken.generate()))
//                .andExpect(status().isOk())
//                .andDo(document("admin/logout",
//                        preprocessRequest(prettyPrint()),
//                        preprocessResponse(prettyPrint()),
//                        requestHeaders(
//                                headerWithName(adminToken.getHeaderName()).description("관리자 인증 토큰")
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
                                .header(adminToken.getHeaderName(), adminToken.generate()))
                .andExpect(status().isOk())
                .andDo(document("admin/auth/refresh",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName(adminToken.getHeaderName()).description("관리자 인증 토큰")
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