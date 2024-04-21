package com.mapshot.api.presentation.admin;

import com.mapshot.api.domain.admin.notice.AdminNoticeService;
import com.mapshot.api.domain.notice.NoticeType;
import com.mapshot.api.infra.auth.annotation.PreAuth;
import com.mapshot.api.infra.auth.enums.Accessible;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@CrossOrigin(originPatterns = {"https://*.kmapshot.com", "https://kmapshot.com"})
@Validated
public class AdminNoticeController {

    private final AdminNoticeService adminNoticeService;

    @PreAuth(Accessible.ADMIN)
    @PostMapping("/notice/register")
    public ResponseEntity<Void> registerNotice(@RequestBody AdminNoticeRequest request) {
        adminNoticeService.saveNotice(NoticeType.valueOf(request.getNoticeType()), request.getTitle(), request.getContent());

        return ResponseEntity.ok().build();
    }

    @PreAuth(Accessible.ADMIN)
    @GetMapping("/notice/delete/{noticeNumber}")
    public ResponseEntity<Void> deleteNotice(@PositiveOrZero @PathVariable(value = "noticeNumber") long noticeNumber) {
        adminNoticeService.deleteNotice(noticeNumber);

        return ResponseEntity.ok().build();
    }


    @PreAuth(Accessible.ADMIN)
    @PostMapping("/notice/modify/{noticeNumber}")
    public ResponseEntity<Void> modifyNotice(@PositiveOrZero @PathVariable(value = "noticeNumber") long noticeNumber,
                                             @RequestBody AdminNoticeRequest request) {
        adminNoticeService.modifyNotice(noticeNumber, NoticeType.valueOf(request.getNoticeType()), request.getTitle(), request.getContent());

        return ResponseEntity.ok().build();
    }

}
