package com.joebrooks.mapshot.controller;

import com.joebrooks.mapshot.model.PostDetailResponse;
import com.joebrooks.mapshot.model.PostSummaryResponse;
import com.joebrooks.mapshot.service.NoticeService;
import java.util.List;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notice")
@CrossOrigin("*") //fixme 테스트 끝나면 삭제할 것
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping("/summary/{postNumber}")
    public ResponseEntity<List<PostSummaryResponse>> showNoticeList(
            @PositiveOrZero @PathVariable(value = "postNumber") long postNumber) {

        List<PostSummaryResponse> postSummaryResponses = noticeService.getMultiplePostsSummary(postNumber);

        return ResponseEntity.ok(postSummaryResponses);
    }

    @GetMapping("/detail/{postNumber}")
    public ResponseEntity<PostDetailResponse> showPost(
            @Positive @PathVariable(value = "postNumber") long postNumber) {

        PostDetailResponse postDetailResponse = noticeService.getSinglePost(postNumber);

        return ResponseEntity.ok(postDetailResponse);
    }
}
