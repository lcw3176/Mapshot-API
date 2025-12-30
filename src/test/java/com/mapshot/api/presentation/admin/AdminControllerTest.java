package com.mapshot.api.presentation.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapshot.api.SlackMockExtension;
import com.mapshot.api.domain.admin.AdminUser;
import com.mapshot.api.domain.admin.AdminUserRepository;
import com.mapshot.api.domain.notice.Notice;
import com.mapshot.api.domain.notice.NoticeRepository;
import com.mapshot.api.domain.notice.NoticeType;
import com.mapshot.api.infra.util.EncryptUtil;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class AdminControllerTest extends SlackMockExtension {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AdminUserRepository adminUserRepository;

    @Autowired
    private NoticeRepository noticeRepository;

    @Value("${jwt.admin.header}")
    private String ADMIN_SESSION;

    private AdminUser testAdminUser;
    private String testPassword = "testPassword123";
    private HttpSession session;

    @BeforeEach
    void setUp() throws Exception {
        String encryptedPassword = EncryptUtil.encrypt(testPassword);
        testAdminUser = AdminUser.builder()
                .userName("testUser")
                .password(encryptedPassword)
                .build();
        adminUserRepository.save(testAdminUser);

        // 로그인하여 세션 생성
        AdminUserRequest loginRequest = AdminUserRequest.builder()
                .nickname("testUser")
                .password(testPassword)
                .build();

        session = mockMvc.perform(
                        RestDocumentationRequestBuilders.post("/admin/user/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getRequest()
                .getSession();
    }

    @AfterEach
    void tearDown() {
        noticeRepository.deleteAll();
        adminUserRepository.deleteAll();
    }

    @Test
    void 로그인_성공() throws Exception {
        // given
        AdminUserRequest request = AdminUserRequest.builder()
                .nickname("testUser")
                .password(testPassword)
                .build();

        // when & then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.post("/admin/user/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(document("admin/login",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("nickname").description("관리자 닉네임"),
                                fieldWithPath("password").description("관리자 비밀번호")
                        )));
    }

    @Test
    void 로그인_실패_존재하지_않는_유저() throws Exception {
        // given
        AdminUserRequest request = AdminUserRequest.builder()
                .nickname("nonExistentUser")
                .password(testPassword)
                .build();

        // when & then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.post("/admin/user/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void 로그인_실패_잘못된_비밀번호() throws Exception {
        // given
        AdminUserRequest request = AdminUserRequest.builder()
                .nickname("testUser")
                .password("wrongPassword")
                .build();

        // when & then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.post("/admin/user/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void 인증_갱신_성공() throws Exception {
        // when & then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.post("/admin/user/auth/refresh")
                                .session((MockHttpSession) session))
                .andExpect(status().isOk())
                .andDo(document("admin/refresh",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    void 인증_갱신_실패_인증되지_않음() throws Exception {
        // when & then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.post("/admin/user/auth/refresh"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void 공지사항_등록_성공() throws Exception {
        // given
        AdminNoticeRequest request = AdminNoticeRequest.builder()
                .noticeType("UPDATE")
                .title("새 공지사항")
                .content("공지사항 내용")
                .build();

        // when & then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.post("/admin/notice/register")
                                .session((MockHttpSession) session)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(document("admin/notice/register",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("noticeType").description("공지사항 타입 (UPDATE, FIX, RESERVED_CHECK)"),
                                fieldWithPath("title").description("공지사항 제목"),
                                fieldWithPath("content").description("공지사항 내용")
                        )));

        // then
        assertThat(noticeRepository.findAll()).hasSize(1);
    }

    @Test
    void 공지사항_등록_실패_인증되지_않음() throws Exception {
        // given
        AdminNoticeRequest request = AdminNoticeRequest.builder()
                .noticeType("UPDATE")
                .title("새 공지사항")
                .content("공지사항 내용")
                .build();

        // when & then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.post("/admin/notice/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void 공지사항_삭제_성공() throws Exception {
        // given
        Notice notice = Notice.builder()
                .noticeType(NoticeType.UPDATE)
                .title("삭제할 공지사항")
                .content("내용")
                .build();
        long noticeId = noticeRepository.save(notice).getId();

        // when & then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.get("/admin/notice/delete/{noticeNumber}", noticeId)
                                .session((MockHttpSession) session))
                .andExpect(status().isOk())
                .andDo(document("admin/notice/delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("noticeNumber").description("삭제할 공지사항 번호")
                        )));

        // then
        assertThat(noticeRepository.findById(noticeId)).isEmpty();
    }

    @Test
    void 공지사항_삭제_실패_존재하지_않는_공지사항() throws Exception {
        // when & then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.get("/admin/notice/delete/{noticeNumber}", -1L)
                                .session((MockHttpSession) session))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void 공지사항_수정_성공() throws Exception {
        // given
        Notice notice = Notice.builder()
                .noticeType(NoticeType.UPDATE)
                .title("원래 제목")
                .content("원래 내용")
                .build();
        long noticeId = noticeRepository.save(notice).getId();

        AdminNoticeRequest request = AdminNoticeRequest.builder()
                .noticeType("FIX")
                .title("수정된 제목")
                .content("수정된 내용")
                .build();

        // when & then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.post("/admin/notice/modify/{noticeNumber}", noticeId)
                                .session((MockHttpSession) session)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(document("admin/notice/modify",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("noticeNumber").description("수정할 공지사항 번호")
                        ),
                        requestFields(
                                fieldWithPath("noticeType").description("공지사항 타입 (UPDATE, FIX, RESERVED_CHECK)"),
                                fieldWithPath("title").description("공지사항 제목"),
                                fieldWithPath("content").description("공지사항 내용")
                        )));

        // then
        Notice modified = noticeRepository.findById(noticeId).orElseThrow();
        assertThat(modified.getTitle()).isEqualTo("수정된 제목");
        assertThat(modified.getContent()).isEqualTo("수정된 내용");
        assertThat(modified.getNoticeType()).isEqualTo(NoticeType.FIX);
    }

    @Test
    void 공지사항_수정_실패_존재하지_않는_공지사항() throws Exception {
        // given
        AdminNoticeRequest request = AdminNoticeRequest.builder()
                .noticeType("UPDATE")
                .title("제목")
                .content("내용")
                .build();

        // when & then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.post("/admin/notice/modify/{noticeNumber}", -1L)
                                .session((MockHttpSession) session)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void 공지사항_수정_실패_인증되지_않음() throws Exception {
        // given
        Notice notice = Notice.builder()
                .noticeType(NoticeType.UPDATE)
                .title("제목")
                .content("내용")
                .build();
        long noticeId = noticeRepository.save(notice).getId();

        AdminNoticeRequest request = AdminNoticeRequest.builder()
                .noticeType("UPDATE")
                .title("수정된 제목")
                .content("수정된 내용")
                .build();

        // when & then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.post("/admin/notice/modify/{noticeNumber}", noticeId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }
}

