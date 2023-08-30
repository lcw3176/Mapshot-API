package com.mapshot.api.board.comment.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapshot.api.SlackMockExtension;
import com.mapshot.api.board.comment.model.CommentRequest;
import com.mapshot.api.board.comment.model.CommentResponse;
import com.mapshot.api.board.comment.repositlry.CommentRepository;
import com.mapshot.api.board.comment.service.CommentService;
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
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
class CommentControllerTest extends SlackMockExtension {


    @Autowired
    ObjectMapper mapper;

    @Autowired
    MockMvc mockMvc;

    private static final String BASE_URL = "/board/comment";

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ContentService contentService;

    @Autowired
    private ContentRepository contentRepository;

    @AfterEach
    void release() {
        commentRepository.deleteAll();
        contentRepository.deleteAll();
    }

    @Test
    void 댓글_목록_조회_테스트() throws Exception {


        long contentId = contentService.save(ContentRequest.builder()
                .title("타이틀")
                .nickname("작성자")
                .content("게시글 내용")
                .build());

        for (int i = 1; i <= 10; i++) {
            CommentRequest request = CommentRequest
                    .builder()
                    .nickname("댓글 작성자")
                    .content("댓글 내용")
                    .contentId(contentId)
                    .referenceCommentId(0L)
                    .build();

            commentService.save(request);
        }

        MvcResult result = mockMvc.perform(
                        RestDocumentationRequestBuilders.get(BASE_URL)
                                .queryParam("contentNumber", String.valueOf(contentId))
                                .queryParam("pageNumber", String.valueOf(1L)))
                .andExpect(status().isOk())
                .andDo(document("board/comment/show",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("contentNumber")
                                        .description("댓글을 조회할 게시글 번호"),
                                parameterWithName("pageNumber")
                                        .description("댓글 페이지 번호")
                        )))
                .andReturn();

        List<CommentResponse> actual = mapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<List<CommentResponse>>() {
                });

        assertThat(actual)
                .hasSize(10)
                .isSortedAccordingTo(Comparator.comparing(CommentResponse::getCreatedDate).reversed());
    }


    @Test
    void 댓글_등록_테스트() throws Exception {

        long contentId = contentService.save(ContentRequest.builder()
                .title("타이틀")
                .nickname("작성자")
                .content("게시글 내용")
                .build());

        CommentRequest request = CommentRequest
                .builder()
                .nickname("댓글 작성자")
                .content("댓글 내용")
                .contentId(contentId)
                .referenceCommentId(0L)
                .build();

        String bodyContent = mapper.writeValueAsString(request);
        mockMvc.perform(
                        RestDocumentationRequestBuilders.post(BASE_URL + "/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(bodyContent))
                .andExpect(status().isOk())
                .andDo(document("board/comment/register",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("nickname")
                                        .description("댓글 작성자 닉네임"),
                                fieldWithPath("content")
                                        .description("댓글 내용"),
                                fieldWithPath("contentId")
                                        .description("해당 댓글이 달린 게시글 id"),
                                fieldWithPath("referenceCommentId")
                                        .description("참조하고 있는 댓글 id(대댓글 기능), 존재하지 않는다면 0을 가짐")
                        )));
//
    }
}