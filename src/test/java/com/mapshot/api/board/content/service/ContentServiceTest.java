package com.mapshot.api.board.content.service;

import com.mapshot.api.board.content.model.ContentDetailResponse;
import com.mapshot.api.board.content.model.ContentListResponse;
import com.mapshot.api.board.content.model.ContentRequest;
import com.mapshot.api.board.content.repository.ContentRepository;
import com.mapshot.api.common.exception.ApiException;
import com.mapshot.api.common.exception.status.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;

@SpringBootTest
class ContentServiceTest {

    @Autowired
    private ContentService contentService;

    @Autowired
    private ContentRepository contentRepository;

    @BeforeEach
    void init() {
        contentRepository.deleteAll();
    }

    @Test
    void 저장() {
        ContentRequest request = ContentRequest
                .builder()
                .title("하이")
                .content("방가방가")
                .writerIpAddress("127.0.0.1")
                .build();

        assertThatNoException()
                .isThrownBy(() -> contentService.save(request));

    }

    @Test
    void 목록_조회() {
        ContentRequest request = ContentRequest
                .builder()
                .title("하이")
                .content("방가방가")
                .writerIpAddress("127.0.0.1")
                .build();

        contentService.save(request);

        List<ContentListResponse> lst = contentService.getContentList(0);

        assertThat(lst).hasSize(1);
    }

    @Test
    void 단일_조회() {
        ContentRequest request = ContentRequest
                .builder()
                .title("하이")
                .content("방가방가")
                .writerIpAddress("127.0.0.1")
                .build();

        long id = contentService.save(request);


        ContentDetailResponse response = contentService.getSingleContent(id);

        assertThat(response.getTitle()).isEqualTo(request.getTitle());
        assertThat(response.getContent()).isEqualTo(request.getContent());
        assertThat(response.getWriterIpAddress()).isEqualTo(request.getWriterIpAddress());
    }

    @Test
    void 없는_게시글_조회시_예외_발생() {

        assertThatThrownBy(() -> contentService.getSingleContent(222))
                .isInstanceOf(ApiException.class)
                .hasMessage(ErrorCode.NO_SUCH_CONTENT.getMessage());
    }
}