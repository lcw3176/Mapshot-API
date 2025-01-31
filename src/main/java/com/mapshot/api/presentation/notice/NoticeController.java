package com.mapshot.api.presentation.notice;


import com.mapshot.api.application.notice.NoticeDetailResponse;
import com.mapshot.api.application.notice.NoticeListResponse;
import com.mapshot.api.application.notice.NoticeUseCase;
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

    private final NoticeUseCase noticeUseCase;

    @GetMapping
    public ResponseEntity<NoticeListResponse> showNoticeList(
            @PositiveOrZero @RequestParam(value = "page", defaultValue = "0", required = false) int page) {

        NoticeListResponse noticeListResponses = noticeUseCase.getNoticeList(page);

        return ResponseEntity.ok(noticeListResponses);
    }

    @GetMapping("/{postNumber}")
    public ResponseEntity<NoticeDetailResponse> showNotice(
            @Positive @PathVariable(value = "postNumber") long postNumber) {

        NoticeDetailResponse noticeDetailResponse = noticeUseCase.getNotice(postNumber);

        return ResponseEntity.ok(noticeDetailResponse);
    }
}
