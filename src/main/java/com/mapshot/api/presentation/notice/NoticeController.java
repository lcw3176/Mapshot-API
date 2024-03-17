package com.mapshot.api.presentation.notice;


import com.mapshot.api.domain.notice.NoticeService;
import com.mapshot.api.infra.auth.annotation.PreAuth;
import com.mapshot.api.infra.auth.enums.Accessible;
import com.mapshot.api.presentation.notice.model.NoticeDetailResponse;
import com.mapshot.api.presentation.notice.model.NoticeListResponse;
import com.mapshot.api.presentation.notice.model.NoticeRegistrationRequest;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notice")
@CrossOrigin(originPatterns = {"https://*.kmapshot.com", "https://kmapshot.com"})
@Validated
public class NoticeController {

    private final NoticeService noticeService;

    @PreAuth(Accessible.EVERYONE)
    @GetMapping("/list/{postNumber}")
    public ResponseEntity<List<NoticeListResponse>> showNoticeList(
            @PositiveOrZero @PathVariable(value = "postNumber") long postNumber) {

        List<NoticeListResponse> noticeListResponses = noticeService.getNoticeList(postNumber);

        return ResponseEntity.ok(noticeListResponses);
    }

    @PreAuth(Accessible.EVERYONE)
    @GetMapping("/detail/{postNumber}")
    public ResponseEntity<NoticeDetailResponse> showNotice(
            @Positive @PathVariable(value = "postNumber") long postNumber) {

        NoticeDetailResponse noticeDetailResponse = noticeService.getSinglePost(postNumber);

        return ResponseEntity.ok(noticeDetailResponse);
    }

    @PreAuth(Accessible.ADMIN)
    @PostMapping("/register")
    public ResponseEntity<Void> registerNotice(@RequestBody NoticeRegistrationRequest noticeRegistrationRequest) {
        noticeService.save(noticeRegistrationRequest);

        return ResponseEntity.ok().build();
    }

    @PreAuth(Accessible.ADMIN)
    @GetMapping("/delete/{noticeNumber}")
    public ResponseEntity<Void> deleteNotice(@PositiveOrZero @PathVariable(value = "noticeNumber") long noticeNumber) {
        noticeService.delete(noticeNumber);

        return ResponseEntity.ok().build();
    }


    @PreAuth(Accessible.ADMIN)
    @PostMapping("/modify/{noticeNumber}")
    public ResponseEntity<Void> modifyNotice(@PositiveOrZero @PathVariable(value = "noticeNumber") long noticeNumber,
                                             @RequestBody NoticeRegistrationRequest noticeRegistrationRequest) {
        noticeService.modify(noticeNumber, noticeRegistrationRequest);

        return ResponseEntity.ok().build();
    }
}
