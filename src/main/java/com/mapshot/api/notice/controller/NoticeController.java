package com.mapshot.api.notice.controller;


import com.mapshot.api.notice.model.NoticeDetailResponse;
import com.mapshot.api.notice.model.NoticeSummaryResponse;
import com.mapshot.api.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notice")
@CrossOrigin(originPatterns = {"https://*.kmapshot.com", "https://kmapshot.com"})
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping("/summary/{postNumber}")
    public ResponseEntity<List<NoticeSummaryResponse>> showNoticeList(
            @PositiveOrZero @PathVariable(value = "postNumber") long postNumber) {

        List<NoticeSummaryResponse> noticeSummaryRespons = noticeService.getMultiplePostsSummary(postNumber);

        return ResponseEntity.ok(noticeSummaryRespons);
    }

    @GetMapping("/detail/{postNumber}")
    public ResponseEntity<NoticeDetailResponse> showPost(
            @Positive @PathVariable(value = "postNumber") long postNumber) {

        NoticeDetailResponse noticeDetailResponse = noticeService.getSinglePost(postNumber);

        return ResponseEntity.ok(noticeDetailResponse);
    }
}
