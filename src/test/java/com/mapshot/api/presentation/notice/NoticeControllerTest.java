package com.mapshot.api.presentation.notice;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapshot.api.SlackMockExtension;
import com.mapshot.api.application.notice.NoticeDetailResponse;
import com.mapshot.api.application.notice.NoticeDto;
import com.mapshot.api.application.notice.NoticeListResponse;
import com.mapshot.api.domain.notice.Notice;
import com.mapshot.api.domain.notice.NoticeRepository;
import com.mapshot.api.domain.notice.NoticeType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Comparator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
class NoticeControllerTest extends SlackMockExtension {

    @Autowired
    ObjectMapper mapper;

    @Autowired
    MockMvc mockMvc;

    private static final String BASE_URL = "/notice";


    @Autowired
    private NoticeRepository noticeRepository;

    @Value("${notice.post.page_size}")
    private int totalSearchSize;

    @BeforeEach
    void init() {
        for (int i = 0; i <= 20; i++) {
            noticeRepository.save(Notice.builder()
                    .noticeType(NoticeType.UPDATE)
                    .title(Integer.toString(i))
                    .content(Integer.toString(i))
                    .build());
        }
    }


    @AfterEach
    void release() {
        noticeRepository.deleteAll();
    }

    @Test
    void 게시글_목록_조회_테스트() throws Exception {
        MvcResult result = mockMvc.perform(
                        RestDocumentationRequestBuilders.get(BASE_URL).queryParam("page", "1"))
                .andExpect(status().isOk())
                .andDo(document("notice/list",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("page")
                                        .description(
                                                "페이지 번호, 10개의 게시글 정보를 최신글부터 반환함")
                        )))
                .andReturn();

        NoticeListResponse actual = mapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<NoticeListResponse>() {
                });

        assertThat(actual.getNotices())
                .hasSize(totalSearchSize)
                .isSortedAccordingTo(Comparator.comparing(NoticeDto::getId).reversed());
    }

    @Test
    void 특정_게시글_조회_테스트() throws Exception {
        long requestId = noticeRepository.findFirstByOrderByIdDesc().getId();

        MvcResult result = mockMvc.perform(
                        RestDocumentationRequestBuilders.get(BASE_URL + "/{postNumber}", requestId))
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
}
