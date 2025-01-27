package com.mapshot.api.presentation.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapshot.api.SlackMockExtension;
import com.mapshot.api.application.auth.Validation;
import com.mapshot.api.domain.admin.user.AdminUserEntity;
import com.mapshot.api.domain.admin.user.AdminUserRepository;
import com.mapshot.api.domain.community.post.PostEntity;
import com.mapshot.api.domain.community.post.PostRepository;
import com.mapshot.api.domain.notice.NoticeEntity;
import com.mapshot.api.domain.notice.NoticeRepository;
import com.mapshot.api.domain.notice.NoticeType;
import com.mapshot.api.infra.util.EncryptUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class AdminControllerTest extends SlackMockExtension {


    private static final String BASE_URL = "/admin";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private Validation adminValidation;
    @Autowired
    private AdminUserRepository adminUserRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private NoticeRepository noticeRepository;
    @Autowired
    private ObjectMapper mapper;
    @Value("${jwt.admin.header}")
    private String ADMIN_HEADER_NAME;

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

        for (int i = 0; i < 100; i++) {
            noticeRepository.save(NoticeEntity.builder()
                    .noticeType(NoticeType.UPDATE)
                    .title(Integer.toString(i))
                    .content(Integer.toString(i))
                    .build());
        }

    }

    @AfterEach
    void release() {
        adminUserRepository.deleteAll();
        postRepository.deleteAll();
        noticeRepository.deleteAll();
    }


    @Test
    void 게시글_삭제_테스트() throws Exception {
        long id = postRepository.findFirstByOrderByIdDesc().getId();

        mockMvc.perform(
                        RestDocumentationRequestBuilders.get(BASE_URL + "/post/delete/{postNumber}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .cookie(adminValidation.makeCookie())
                )
                .andExpect(status().isOk())
                .andDo(document("post/delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
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


    @Test
    void 게시글_등록_테스트() throws Exception {
        AdminNoticeRequest request = AdminNoticeRequest
                .builder()
                .title("헬로")
                .content("방가방가")
                .noticeType(NoticeType.FIX.toString())
                .build();

        mockMvc.perform(
                        RestDocumentationRequestBuilders.post(BASE_URL + "/notice/register")
                                .content(mapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                                .cookie(adminValidation.makeCookie())
                )
                .andExpect(status().isOk())
                .andDo(document("notice/register",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("title").description("공지사항 제목"),
                                fieldWithPath("content").description("공지사항 내용"),
                                fieldWithPath("noticeType").description("공지사항 종류")
                        )));
    }

    @Test
    void 관리자가_아닌_사람이_등록_요청시_예외() throws Exception {
        AdminNoticeRequest request = AdminNoticeRequest
                .builder()
                .title("헬로")
                .content("방가방가")
                .noticeType(NoticeType.FIX.toString())
                .build();

        mockMvc.perform(
                        post(BASE_URL + "/notice/register")
                                .content(mapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void 공시자항_삭제_테스트() throws Exception {
        long id = noticeRepository.findFirstByOrderByIdDesc().getId();

        mockMvc.perform(
                        RestDocumentationRequestBuilders.get(BASE_URL + "/notice/delete/{noticeNumber}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .cookie(adminValidation.makeCookie())
                )
                .andExpect(status().isOk())
                .andDo(document("notice/delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("noticeNumber").description("게시글 번호")
                        )));
    }

    @Test
    void 관리자가_아닌_사람이_공지사항_삭제_요청시_예외() throws Exception {
        long id = noticeRepository.findFirstByOrderByIdDesc().getId();

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/notice/delete/{noticeNumber}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void 공지사항_수정_테스트() throws Exception {
        long id = noticeRepository.findFirstByOrderByIdDesc().getId();

        AdminNoticeRequest request = AdminNoticeRequest.builder()
                .noticeType(NoticeType.RESERVED_CHECK.toString())
                .title("수정된 타이틀")
                .content("수정된 컨텐츠")
                .build();

        mockMvc.perform(
                        RestDocumentationRequestBuilders.post(BASE_URL + "/notice/modify/{noticeNumber}", id)
                                .content(mapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                                .cookie(adminValidation.makeCookie())
                )
                .andExpect(status().isOk())
                .andDo(document("notice/modify",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("title").description("수정할 공지사항 제목"),
                                fieldWithPath("content").description("수정할 공지사항 내용"),
                                fieldWithPath("noticeType").description("수정할 공지사항 종류")
                        )));
    }


    @Test
    void 관리자가_아닌_사람이_공지사항_수정_요청시_예외() throws Exception {
        long id = noticeRepository.findFirstByOrderByIdDesc().getId();

        AdminNoticeRequest request = AdminNoticeRequest.builder()
                .noticeType(NoticeType.RESERVED_CHECK.toString())
                .title("수정된 타이틀")
                .content("수정된 컨텐츠")
                .build();

        mockMvc.perform(post(BASE_URL + "/notice/modify/{noticeNumber}", id)
                        .content(mapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
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
    void 토큰_없이_관리자_로그인_연장_요청시_예외() throws Exception {
        mockMvc.perform(
                        post(BASE_URL + "/user/auth/refresh")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

}