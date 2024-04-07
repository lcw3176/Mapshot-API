package com.mapshot.api.presentation.notice;


import com.mapshot.api.domain.notice.NoticeDetailResponse;
import com.mapshot.api.domain.notice.NoticeListResponse;
import com.mapshot.api.domain.notice.NoticeService;
import com.mapshot.api.infra.auth.annotation.PreAuth;
import com.mapshot.api.infra.auth.enums.Accessible;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notice")
@CrossOrigin(originPatterns = {"https://*.kmapshot.com", "https://kmapshot.com"})
@Validated
public class NoticeController {

    private final NoticeService noticeService;

    @PreAuth(Accessible.EVERYONE)
    @GetMapping
    public ResponseEntity<NoticeListResponse> showNoticeList(
            @PositiveOrZero @RequestParam(value = "page", defaultValue = "0") int page) {

        NoticeListResponse noticeListResponses = noticeService.getNoticeByPageNumber(page);

        return ResponseEntity.ok(noticeListResponses);
    }

    @PreAuth(Accessible.EVERYONE)
    @GetMapping("/{postNumber}")
    public ResponseEntity<NoticeDetailResponse> showNotice(
            @Positive @PathVariable(value = "postNumber") long postNumber) {

        NoticeDetailResponse noticeDetailResponse = noticeService.getSinglePost(postNumber);

        return ResponseEntity.ok(noticeDetailResponse);
    }
}
