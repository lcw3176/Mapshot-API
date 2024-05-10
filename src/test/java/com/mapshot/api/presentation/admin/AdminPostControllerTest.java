package com.mapshot.api.presentation.admin;

import com.mapshot.api.SlackMockExtension;
import com.mapshot.api.domain.admin.user.AdminUserEntity;
import com.mapshot.api.domain.admin.user.AdminUserRepository;
import com.mapshot.api.domain.community.post.PostEntity;
import com.mapshot.api.domain.community.post.PostRepository;
import com.mapshot.api.infra.auth.Validation;
import com.mapshot.api.infra.encrypt.EncryptUtil;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class AdminPostControllerTest extends SlackMockExtension {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Validation adminValidation;

    @Autowired
    private AdminUserRepository adminUserRepository;

    @Autowired
    private PostRepository postRepository;

    @Value("${jwt.admin.header}")
    private String ADMIN_HEADER_NAME;

    private static final String BASE_URL = "/admin";

    @BeforeEach
    void init() {
        adminUserRepository.save(AdminUserEntity.builder()
                .nickname("test")
                .password(EncryptUtil.encrypt("1234"))
                .build());

        for (int i = 0; i < 100; i++) {
            String temp = Integer.toString(i);
            postRepository.save(PostEntity.builder()
                    .title(temp)
                    .writer(temp)
                    .content(temp)
                    .commentCount(0)
                    .password(temp)
                    .deleted(false)
                    .build());
        }
    }

    @AfterEach
    void release() {
        adminUserRepository.deleteAll();
        postRepository.deleteAll();
    }


    @Test
    void 게시글_삭제_테스트() throws Exception {
        long id = postRepository.findFirstByOrderByIdDesc().getId();

        mockMvc.perform(
                        RestDocumentationRequestBuilders.get(BASE_URL + "/post/delete/{postNumber}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(HttpHeaders.readOnlyHttpHeaders(adminValidation.makeHeader())))
                .andExpect(status().isOk())
                .andDo(document("post/delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName(ADMIN_HEADER_NAME).description("관리자 인증 토큰")
                        ),
                        pathParameters(
                                parameterWithName("postNumber").description("게시글 번호")
                        )));
    }

    @Test
    void 관리자가_아닌_사람이_삭제_요청시_예외() throws Exception {
        long id = postRepository.findFirstByOrderByIdDesc().getId();

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/post/delete/{postNumber}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

}