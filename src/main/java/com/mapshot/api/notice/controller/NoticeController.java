package com.mapshot.api.notice.controller;


import com.mapshot.api.common.annotation.PreAuth;
import com.mapshot.api.common.enums.AuthType;
import com.mapshot.api.notice.model.NoticeDetailResponse;
import com.mapshot.api.notice.model.NoticeRequest;
import com.mapshot.api.notice.model.NoticeSummaryResponse;
import com.mapshot.api.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notice")
@CrossOrigin(originPatterns = {"https://*.kmapshot.com", "https://kmapshot.com"})
@Validated
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping("/summary/{postNumber}")
    public ResponseEntity<List<NoticeSummaryResponse>> showNoticeList(
            @PositiveOrZero @PathVariable(value = "postNumber") long postNumber) {

        List<NoticeSummaryResponse> noticeSummaryRespons = noticeService.getMultiplePostsSummary(postNumber);

        return ResponseEntity.ok(noticeSummaryRespons);
    }

    @GetMapping("/detail/{postNumber}")
    public ResponseEntity<NoticeDetailResponse> showNotice(
            @Positive @PathVariable(value = "postNumber") long postNumber) {

        NoticeDetailResponse noticeDetailResponse = noticeService.getSinglePost(postNumber);

        return ResponseEntity.ok(noticeDetailResponse);
    }

    @PreAuth(AuthType.ADMIN)
    @PostMapping("/register")
    public ResponseEntity<Void> registerNotice(@RequestBody NoticeRequest noticeRequest) {
        noticeService.save(noticeRequest);

        return ResponseEntity.ok().build();
    }

    @PreAuth(AuthType.ADMIN)
    @GetMapping("/delete/{noticeNumber}")
    public ResponseEntity<Void> deleteNotice(@PositiveOrZero @PathVariable(value = "noticeNumber") long noticeNumber) {
        noticeService.delete(noticeNumber);

        return ResponseEntity.ok().build();
    }


    @PreAuth(AuthType.ADMIN)
    @PostMapping("/modify/{noticeNumber}")
    public ResponseEntity<Void> modifyNotice(@PositiveOrZero @PathVariable(value = "noticeNumber") long noticeNumber,
                                             @RequestBody NoticeRequest noticeRequest) {
        noticeService.modify(noticeNumber, noticeRequest);

        return ResponseEntity.ok().build();
    }
}
