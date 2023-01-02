package com.joebrooks.mapshotapi.notice.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joebrooks.mapshotapi.repository.notice.NoticeEntity;
import java.util.Comparator;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;


@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
class NoticeControllerTest {

    @Autowired
    ObjectMapper mapper;

    @Autowired
    MockMvc mockMvc;


    private static final String BASE_URL = "/api/notice";

    @Test
    void 게시글_목록_조회_테스트() throws Exception {
        MvcResult result = mockMvc.perform(RestDocumentationRequestBuilders.get(BASE_URL + "/{contentNumber}", 100))
                .andExpect(status().isOk())
                .andDo(document("notice/page",
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("contentNumber")
                                        .description(
                                                "게시글 번호, 요청한 번호를 기준으로 10개의 게시글 정보를 최신글부터 내림차순으로 반환함")
                        )))
                .andReturn();

        List<NoticeEntity> actual = mapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<List<NoticeEntity>>() {
                });

        assertThat(actual)
                .hasSize(10)
                .isSortedAccordingTo(Comparator.comparing(NoticeEntity::getId).reversed());
    }

    @Test
    void 특정_게시글_조회_테스트() throws Exception {
        long requestId = 100;

        MvcResult result = mockMvc.perform(
                        RestDocumentationRequestBuilders.get(BASE_URL + "/content/{contentNumber}", requestId))
                .andExpect(status().isOk())
                .andDo(document("notice/content",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("contentNumber")
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

        NoticeEntity actual = mapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<NoticeEntity>() {
                });

        assertThat(actual.getId()).isEqualTo(requestId);
    }
}