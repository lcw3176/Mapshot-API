package com.mapshot.api.board.content.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapshot.api.SlackMockExtension;
import com.mapshot.api.board.content.model.ContentListResponse;
import com.mapshot.api.board.content.model.ContentRequest;
import com.mapshot.api.board.content.repository.ContentRepository;
import com.mapshot.api.board.content.service.ContentService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
class ContentControllerTest extends SlackMockExtension {


    @Autowired
    ObjectMapper mapper;

    @Autowired
    MockMvc mockMvc;

    private static final String BASE_URL = "/board/content";

    @Autowired
    private ContentService contentService;

    @Autowired
    private ContentRepository contentRepository;


    @AfterEach
    void release() {
        contentRepository.deleteAll();
    }

    @Test
    void 게시글_목록_조회_테스트() throws Exception {
        for (int i = 1; i <= 10; i++) {
            ContentRequest request = ContentRequest
                    .builder()
                    .title("하이")
                    .content("방가방가")
                    .nickname("1234567890")
                    .build();

            contentService.save(request);
        }

        MvcResult result = mockMvc.perform(
                        RestDocumentationRequestBuilders.get(BASE_URL + "/list/{pageNumber}", 1))
                .andExpect(status().isOk())
                .andDo(document("board/content/list",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("pageNumber")
                                        .description(
                                                "페이지 번호, 10개의 게시글 정보를 최신순으로 반환함")
                        )))
                .andReturn();

        List<ContentListResponse> actual = mapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<List<ContentListResponse>>() {
                });

        assertThat(actual)
                .hasSize(10)
                .isSortedAccordingTo(Comparator.comparing(ContentListResponse::getId).reversed());
    }


    @Test
    void 특정_게시글_조회_테스트() throws Exception {
        ContentRequest request = ContentRequest
                .builder()
                .title("특정 조회 타이틀")
                .content("특정 조회 내용")
                .nickname("특정 조회 닉네임")
                .build();

        long id = contentService.save(request);


        mockMvc.perform(
                        RestDocumentationRequestBuilders.get(BASE_URL + "/{id}", id))
                .andExpect(status().isOk())
                .andDo(document("board/content/detail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("id")
                                        .description(
                                                "게시글 id")
                        )));

    }

    @Test
    void 게시글_등록_테스트() throws Exception {
        ContentRequest request = ContentRequest
                .builder()
                .title("등록 타이틀")
                .content("등록 내용")
                .nickname("등록 닉네임")
                .build();

        String bodyContent = mapper.writeValueAsString(request);

        mockMvc.perform(
                        RestDocumentationRequestBuilders.post(BASE_URL + "/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(bodyContent))
                .andExpect(status().isOk())
                .andDo(document("board/content/register",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("title")
                                        .description("게시글 제목"),
                                fieldWithPath("content")
                                        .description("게시글 내용"),
                                fieldWithPath("nickname")
                                        .description("작성자 닉네임")
                        )));

        List<ContentListResponse> contents = contentService.getContentList(1);

        assertEquals(contents.get(0).getTitle(), request.getTitle());
    }
}