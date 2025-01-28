package com.mapshot.api.presentation.community.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapshot.api.SlackMockExtension;
import com.mapshot.api.domain.community.comment.CommentEntity;
import com.mapshot.api.domain.community.comment.CommentRepository;
import com.mapshot.api.domain.community.post.PostEntity;
import com.mapshot.api.domain.community.post.PostRepository;
import com.mapshot.api.infra.util.EncryptUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class CommentControllerTest extends SlackMockExtension {


    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    private static final String BASE_URL = "/comment";

    @BeforeEach
    void init() {

        for (int i = 0; i < 10; i++) {
            String temp = Integer.toString(i);

            long postId = postRepository.save(PostEntity.builder()
                    .commentCount(0)
                    .title(temp)
                    .deleted(false)
                    .content(temp)
                    .writer(temp)
                    .password(EncryptUtil.encrypt(temp))
                    .build()).getId();

            for (int j = 0; j < 10; j++) {
                String commentTemp = Integer.toString(j);

                commentRepository.save(CommentEntity.builder()
                        .postId(postId)
                        .writer(commentTemp)
                        .content(commentTemp)
                        .deleted(false)
                        .password(EncryptUtil.encrypt(commentTemp))
                        .build());
            }
        }
    }

    @AfterEach
    void release() {
        commentRepository.deleteAll();
    }

    @Test
    void 댓글_조회_테스트() throws Exception {
        long postId = postRepository.findFirstByOrderByIdDesc().getId();

        mockMvc.perform(
                        RestDocumentationRequestBuilders.get(BASE_URL)
                                .queryParam("page", "0")
                                .queryParam("postId", Long.toString(postId)))
                .andExpect(status().isOk())
                .andDo(document("comment/list",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("page").description("페이지 번호"),
                                parameterWithName("postId").description("게시글 id")
                        ),
                        responseFields(
                                fieldWithPath("totalPage").description("페이지 수"),
                                fieldWithPath("comments[].id").description("댓글 아이디"),
                                fieldWithPath("comments[].writer").description("댓글 작성자"),
                                fieldWithPath("comments[].content").description("댓글 내용"),
                                fieldWithPath("comments[].createdDate").description("댓글 작성 시각")
                        )));
    }


    @Test
    void 댓글_등록_테스트() throws Exception {
        long postId = postRepository.findFirstByOrderByIdDesc().getId();
        CommentRegisterRequest request = CommentRegisterRequest
                .builder()
                .postId(postId)
                .writer("헬로")
                .content("방가방가")
                .password("hello")
                .build();

        mockMvc.perform(
                        RestDocumentationRequestBuilders.post(BASE_URL + "/register")
                                .content(mapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("comment/register",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("postId").description("게시글 아이디"),
                                fieldWithPath("writer").description("댓글 작성자"),
                                fieldWithPath("content").description("댓글 내용"),
                                fieldWithPath("password").description("댓글 비밀번호")
                        )));
    }

    @Test
    void 작성자가_아닌_사람이_삭제_요청시_예외() throws Exception {
        long postId = postRepository.findFirstByOrderByIdDesc().getId();

        long commentId = commentRepository.save(CommentEntity.builder()
                .writer("hello")
                .postId(postId)
                .content("hello")
                .deleted(false)
                .password(EncryptUtil.encrypt("hello"))
                .build()).getId();

        mockMvc.perform(
                        get(BASE_URL + "/delete/{commentId}", commentId)
                                .queryParam("password", "wrong password"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void 댓글_삭제_테스트() throws Exception {
        long postId = postRepository.findFirstByOrderByIdDesc().getId();

        long commentId = commentRepository.save(CommentEntity.builder()
                .writer("hello")
                .postId(postId)
                .content("hello")
                .deleted(false)
                .password(EncryptUtil.encrypt("hello"))
                .build()).getId();


        mockMvc.perform(
                        RestDocumentationRequestBuilders.get(BASE_URL + "/delete/{commentId}", commentId)
                                .queryParam("password", "hello"))
                .andExpect(status().isOk())
                .andDo(document("comment/delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("commentId").description("댓글 아이디")
                        ),
                        queryParameters(
                                parameterWithName("password").description("댓글 비밀번호")
                        )));
    }


}