package com.mapshot.api.presentation.community.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapshot.api.SlackMockExtension;
import com.mapshot.api.domain.community.post.PostEntity;
import com.mapshot.api.domain.community.post.PostRepository;
import com.mapshot.api.infra.encrypt.EncryptUtil;
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
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class PostControllerTest extends SlackMockExtension {


    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    private static final String BASE_URL = "/post";

    @BeforeEach
    void init() {

        for (int i = 0; i < 100; i++) {
            String temp = Integer.toString(i);

            postRepository.save(PostEntity.builder()
                    .commentCount(0)
                    .title(temp)
                    .deleted(false)
                    .content(temp)
                    .writer(temp)
                    .password(EncryptUtil.encrypt(temp))
                    .build());
        }
    }

    @AfterEach
    void release() {
        postRepository.deleteAll();
    }


    @Test
    void 게시글_등록_테스트() throws Exception {
        PostRegisterRequest request = PostRegisterRequest.builder()
                .title("hello")
                .writer("good")
                .content("hi")
                .password("password")
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
                                fieldWithPath("title").description("게시글 제목"),
                                fieldWithPath("writer").description("게시글 작성자"),
                                fieldWithPath("content").description("게시글 내용"),
                                fieldWithPath("password").description("게시글 비밀번호")
                        )));
    }


    @Test
    void 작성자가_아닌_사람이_삭제_요청시_예외() throws Exception {
        long postId = postRepository.findFirstByOrderByIdDesc().getId();

        mockMvc.perform(
                        get(BASE_URL + "/delete/{postNumber}", postId)
                                .queryParam("password", "wrong password"))
                .andExpect(status().is4xxClientError());
    }


    @Test
    void 게시글_삭제_테스트() throws Exception {
        long postId = postRepository.save(PostEntity.builder()
                .commentCount(0)
                .title("hello")
                .deleted(false)
                .content("hello")
                .writer("hello")
                .password(EncryptUtil.encrypt("password"))
                .build()).getId();

        mockMvc.perform(
                        get(BASE_URL + "/delete/{postNumber}", postId)
                                .queryParam("password", "password"))
                .andExpect(status().isOk())
                .andDo(document("post/delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("postNumber").description("게시글 아이디")
                        ),
                        queryParameters(
                                parameterWithName("password").description("게시글 비밀번호")
                        )));
    }

    @Test
    void 게시글_목록_조회_테스트() throws Exception {

        mockMvc.perform(
                        RestDocumentationRequestBuilders.get(BASE_URL)
                                .queryParam("page", "0"))
                .andExpect(status().isOk())
                .andDo(document("post/list",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("totalPage").description("페이지 수"),
                                fieldWithPath("posts[].id").description("게시글 아이디"),
                                fieldWithPath("posts[].writer").description("게시글 작성자"),
                                fieldWithPath("posts[].title").description("게시글 제목"),
                                fieldWithPath("posts[].commentCount").description("게시글 내용"),
                                fieldWithPath("posts[].createdDate").description("게시글 작성 시각")
                        )));
    }


    @Test
    void 게시글_단건_조회_테스트() throws Exception {
        long postId = postRepository.findFirstByOrderByIdDesc().getId();

        mockMvc.perform(
                        RestDocumentationRequestBuilders.get(BASE_URL + "/{id}", postId))
                .andExpect(status().isOk())
                .andDo(document("post/detail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("id").description("게시글 아이디"),
                                fieldWithPath("writer").description("게시글 작성자"),
                                fieldWithPath("content").description("게시글 내용"),
                                fieldWithPath("title").description("게시글 제목"),
                                fieldWithPath("createdDate").description("게시글 작성 시각")
                        )));
    }

}