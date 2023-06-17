package com.mapshot.api.notice.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapshot.api.common.token.JwtUtil;
import com.mapshot.api.notice.enums.NoticeType;
import com.mapshot.api.notice.model.NoticeDetailResponse;
import com.mapshot.api.notice.model.NoticeRequest;
import com.mapshot.api.notice.model.NoticeSummaryResponse;
import com.mapshot.api.notice.service.NoticeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@EnableJpaAuditing
class NoticeControllerTest {

    @Autowired
    ObjectMapper mapper;

    @Autowired
    MockMvc mockMvc;

    private static final String BASE_URL = "/notice";

    @Autowired
    private NoticeService noticeService;


    @Test
    void 게시글_목록_조회_테스트() throws Exception {
        MvcResult result = mockMvc.perform(
                        RestDocumentationRequestBuilders.get(BASE_URL + "/summary/{startPostNumber}", 11))
                .andExpect(status().isOk())
                .andDo(document("notice/summary",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("startPostNumber")
                                        .description(
                                                "게시글 번호, 요청한 번호 미만으로 10개의 게시글 정보를 최신글부터 반환함")
                        )))
                .andReturn();

        List<NoticeSummaryResponse> actual = mapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<List<NoticeSummaryResponse>>() {
                });

        assertThat(actual)
                .hasSize(10)
                .isSortedAccordingTo(Comparator.comparing(NoticeSummaryResponse::getId).reversed());
    }

    @Test
    void 특정_게시글_조회_테스트() throws Exception {
        long requestId = 2;

        MvcResult result = mockMvc.perform(
                        RestDocumentationRequestBuilders.get(BASE_URL + "/detail/{postNumber}", requestId))
                .andExpect(status().isOk())
                .andDo(document("notice/detail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("postNumber")
                                        .description("게시글 번호")
                        ),
                        responseFields(
                                fieldWithPath("id").description("공지사항 번호"),
                                fieldWithPath("noticeType").description("공지사항 종류"),
                                fieldWithPath("title").description("공지사항 제목"),
                                fieldWithPath("content").description("공지사항 내용"),
                                fieldWithPath("createdDate").description("작성된 시각")
                        )))
                .andReturn();

        NoticeDetailResponse actual = mapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<NoticeDetailResponse>() {
                });

        assertThat(actual.getId()).isEqualTo(requestId);
    }

    @Test
    void 게시글_등록_테스트() throws Exception {
        NoticeRequest request = NoticeRequest
                .builder()
                .title("헬로")
                .content("방가방가")
                .noticeType(NoticeType.FIX.toString())
                .build();

        mockMvc.perform(
                        RestDocumentationRequestBuilders.post(BASE_URL + "/register")
                                .content(mapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(JwtUtil.ADMIN_HEADER_NAME, JwtUtil.generateAdmin()))
                .andExpect(status().isOk())
                .andDo(document("notice/register",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName(JwtUtil.ADMIN_HEADER_NAME).description("관리자 인증 토큰")
                        ),
                        requestFields(
                                fieldWithPath("title").description("공지사항 제목"),
                                fieldWithPath("content").description("공지사항 내용"),
                                fieldWithPath("noticeType").description("공지사항 종류")
                        )));
    }

    @Test
    void 관리자가_아닌_사람이_등록_요청시_예외() throws Exception {
        NoticeRequest request = NoticeRequest
                .builder()
                .title("헬로")
                .content("방가방가")
                .noticeType(NoticeType.FIX.toString())
                .build();

        mockMvc.perform(
                        post(BASE_URL + "/register")
                                .content(mapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void 게시글_삭제_테스트() throws Exception {
        NoticeSummaryResponse mostRecentNotice = noticeService.getMultiplePostsSummary(0).get(0);

        mockMvc.perform(
                        RestDocumentationRequestBuilders.get(BASE_URL + "/delete/{noticeNumber}", mostRecentNotice.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(JwtUtil.ADMIN_HEADER_NAME, JwtUtil.generateAdmin()))
                .andExpect(status().isOk())
                .andDo(document("notice/delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName(JwtUtil.ADMIN_HEADER_NAME).description("관리자 인증 토큰")
                        ),
                        pathParameters(
                                parameterWithName("noticeNumber").description("게시글 번호")
                        )));
    }

    @Test
    void 관리자가_아닌_사람이_삭제_요청시_예외() throws Exception {
        NoticeSummaryResponse mostRecentNotice = noticeService.getMultiplePostsSummary(0).get(0);

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/delete/{noticeNumber}", mostRecentNotice.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void 게시글_수정_테스트() throws Exception {
        NoticeSummaryResponse mostRecentNotice = noticeService.getMultiplePostsSummary(0).get(0);

        NoticeRequest request = NoticeRequest.builder()
                .noticeType(NoticeType.RESERVED_CHECK.toString())
                .title("수정된 타이틀")
                .content("수정된 컨텐츠")
                .build();

        mockMvc.perform(
                        RestDocumentationRequestBuilders.post(BASE_URL + "/modify/{noticeNumber}", mostRecentNotice.getId())
                                .content(mapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(JwtUtil.ADMIN_HEADER_NAME, JwtUtil.generateAdmin()))
                .andExpect(status().isOk())
                .andDo(document("notice/modify",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName(JwtUtil.ADMIN_HEADER_NAME).description("관리자 인증 토큰")
                        ),
                        requestFields(
                                fieldWithPath("title").description("수정할 공지사항 제목"),
                                fieldWithPath("content").description("수정할 공지사항 내용"),
                                fieldWithPath("noticeType").description("수정할 공지사항 종류")
                        )));
    }


    @Test
    void 관리자가_아닌_사람이_수정_요청시_예외() throws Exception {
        NoticeSummaryResponse mostRecentNotice = noticeService.getMultiplePostsSummary(0).get(0);

        NoticeRequest request = NoticeRequest.builder()
                .noticeType(NoticeType.RESERVED_CHECK.toString())
                .title("수정된 타이틀")
                .content("수정된 컨텐츠")
                .build();

        mockMvc.perform(post(BASE_URL + "/modify/{noticeNumber}", mostRecentNotice.getId())
                        .content(mapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }
}