package com.mapshot.api.board.comment.service;

import com.mapshot.api.board.comment.model.CommentRequest;
import com.mapshot.api.board.comment.model.CommentResponse;
import com.mapshot.api.board.comment.repositlry.CommentRepository;
import com.mapshot.api.board.content.model.ContentRequest;
import com.mapshot.api.board.content.service.ContentService;
import com.mapshot.api.common.exception.ApiException;
import com.mapshot.api.common.exception.status.ErrorCode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private ContentService contentService;

    @Autowired
    private CommentRepository commentRepository;


    @AfterEach
    void init() {
        commentRepository.deleteAll();
    }

    private long generateContent() {
        long contentId = contentService.save(ContentRequest.builder()
                .title("타이틀")
                .content("내용")
                .nickname("닉네임")
                .build());

        return contentId;
    }

    private long saveComment(long contentId) {
        return commentService.save(CommentRequest.builder()
                .contentId(contentId)
                .referenceCommentId(0L)
                .nickname("닉네임")
                .content("내용")
                .build());
    }

    @Test
    void 저장() {

        long contentId = generateContent();

        CommentRequest request = CommentRequest
                .builder()
                .referenceCommentId(0L)
                .contentId(contentId)
                .content("방가방가")
                .nickname("1234567890")
                .build();

        assertThatNoException()
                .isThrownBy(() -> commentService.save(request));

    }


    @Test
    void 목록_조회() {
        long contentId = generateContent();

        for (int i = 1; i <= 10; i++) {
            saveComment(contentId);
        }

        List<CommentResponse> lst = commentService.getComments(contentId, 1);

        assertThat(lst).hasSize(10)
                .isSortedAccordingTo(Comparator.comparing(CommentResponse::getCreatedDate).reversed());
    }

    @Test
    void 대댓글_저장() {

        long contentId = generateContent();

        CommentRequest request = CommentRequest
                .builder()
                .referenceCommentId(0L)
                .contentId(contentId)
                .content("방가방가")
                .nickname("1234567890")
                .build();

        long commentId = commentService.save(request);

        CommentRequest reRequest = CommentRequest
                .builder()
                .referenceCommentId(commentId)
                .contentId(contentId)
                .content("대댓글~")
                .nickname("1234567890")
                .build();

        commentService.save(reRequest);

        List<CommentResponse> commentResponses = commentService.getComments(contentId, 1);

        assertEquals(commentResponses.get(0).getReferenceCommentId(), commentId);
    }

    @Test
    void 페이지_1미만으로_요청시_예외_발생() {

        long contentId = generateContent();
        long commentId = saveComment(contentId);

        CommentRequest reRequest = CommentRequest
                .builder()
                .referenceCommentId(0L)
                .contentId(commentId)
                .content("대댓글~")
                .nickname("1234567890")
                .build();

        commentService.save(reRequest);

        assertThatThrownBy(() -> commentService.getComments(contentId, 0))
                .isInstanceOf(ApiException.class)
                .hasMessage(ErrorCode.NO_SUCH_COMMENT.getMessage());
    }
    
}