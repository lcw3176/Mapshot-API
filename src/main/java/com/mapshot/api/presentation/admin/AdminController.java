package com.mapshot.api.presentation.admin;

import com.mapshot.api.domain.admin.AdminService;
import com.mapshot.api.domain.notice.NoticeType;
import com.mapshot.api.infra.auth.annotation.PreAuth;
import com.mapshot.api.infra.auth.enums.Accessible;
import com.mapshot.api.presentation.notice.NoticeRegistrationRequest;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@CrossOrigin(originPatterns = {"https://*.kmapshot.com", "https://kmapshot.com"})
@Validated
public class AdminController {

    private final AdminService adminService;

    @PreAuth(Accessible.EVERYONE)
    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody AdminRequest request) {
        adminService.validateUser(request.getNickname(), request.getPassword());
        HttpHeaders authHeader = adminService.getAuthHeader();

        return ResponseEntity.ok()
                .headers(authHeader)
                .build();
    }

    @PreAuth(Accessible.ADMIN)
    @PostMapping("/auth/refresh")
    public ResponseEntity<Void> refreshAuth() {
        HttpHeaders authHeader = adminService.getAuthHeader();

        return ResponseEntity.ok()
                .headers(authHeader)
                .build();
    }

    @PreAuth(Accessible.ADMIN)
    @PostMapping("/notice/register")
    public ResponseEntity<Void> registerNotice(@RequestBody NoticeRegistrationRequest request) {
        adminService.saveNotice(NoticeType.valueOf(request.getNoticeType()), request.getTitle(), request.getContent());

        return ResponseEntity.ok().build();
    }

    @PreAuth(Accessible.ADMIN)
    @GetMapping("/notice/delete/{noticeNumber}")
    public ResponseEntity<Void> deleteNotice(@PositiveOrZero @PathVariable(value = "noticeNumber") long noticeNumber) {
        adminService.deleteNotice(noticeNumber);

        return ResponseEntity.ok().build();
    }


    @PreAuth(Accessible.ADMIN)
    @PostMapping("/notice/modify/{noticeNumber}")
    public ResponseEntity<Void> modifyNotice(@PositiveOrZero @PathVariable(value = "noticeNumber") long noticeNumber,
                                             @RequestBody NoticeRegistrationRequest request) {
        adminService.modifyNotice(noticeNumber, NoticeType.valueOf(request.getNoticeType()), request.getTitle(), request.getContent());

        return ResponseEntity.ok().build();
    }


    @PreAuth(Accessible.ADMIN)
    @GetMapping("/post/delete/{postNumber}")
    public ResponseEntity<Void> deletePost(@Positive @PathVariable(value = "postNumber") long postNumber) {
        adminService.deletePost(postNumber);

        return ResponseEntity.ok().build();
    }
}
