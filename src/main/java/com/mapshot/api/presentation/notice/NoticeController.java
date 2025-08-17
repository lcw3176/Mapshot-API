package com.mapshot.api.presentation.notice;


import com.mapshot.api.application.notice.NoticeDetailResponse;
import com.mapshot.api.application.notice.NoticeFacade;
import com.mapshot.api.application.notice.NoticeListResponse;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notice")
@Validated
public class NoticeController {

    private final NoticeFacade noticeFacade;

    @GetMapping
    public ResponseEntity<NoticeListResponse> showNoticeList(
            @PositiveOrZero @RequestParam(value = "page", defaultValue = "0", required = false) int page) {

        NoticeListResponse noticeListResponses = noticeFacade.getNoticeList(page);

        return ResponseEntity.ok(noticeListResponses);
    }

    @GetMapping("/{postNumber}")
    public ResponseEntity<NoticeDetailResponse> showNotice(
            @Positive @PathVariable(value = "postNumber") long postNumber) {

        NoticeDetailResponse noticeDetailResponse = noticeFacade.getNotice(postNumber);

        return ResponseEntity.ok(noticeDetailResponse);
    }
}
