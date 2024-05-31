package com.mapshot.api.presentation.admin;

import com.mapshot.api.application.admin.AdminUseCase;
import com.mapshot.api.domain.notice.NoticeType;
import com.mapshot.api.infra.auth.annotation.PreAuth;
import com.mapshot.api.infra.auth.enums.Accessible;
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

    private final AdminUseCase adminUseCase;

    @PreAuth(Accessible.EVERYONE)
    @PostMapping("/user/login")
    public ResponseEntity<Void> login(@RequestBody AdminUserRequest request) {
        HttpHeaders authHeader = adminUseCase.login(request.getNickname(), request.getPassword());

        return ResponseEntity.ok()
                .headers(authHeader)
                .build();
    }

    @PreAuth(Accessible.ADMIN)
    @PostMapping("/user/auth/refresh")
    public ResponseEntity<Void> refreshAuth() {
        HttpHeaders authHeader = adminUseCase.getAuth();

        return ResponseEntity.ok()
                .headers(authHeader)
                .build();
    }


    @PreAuth(Accessible.ADMIN)
    @PostMapping("/notice/register")
    public ResponseEntity<Void> registerNotice(@RequestBody AdminNoticeRequest request) {
        adminUseCase.saveNotice(NoticeType.valueOf(request.getNoticeType()), request.getTitle(), request.getContent());

        return ResponseEntity.ok().build();
    }

    @PreAuth(Accessible.ADMIN)
    @GetMapping("/notice/delete/{noticeNumber}")
    public ResponseEntity<Void> deleteNotice(@PositiveOrZero @PathVariable(value = "noticeNumber") long noticeNumber) {
        adminUseCase.deleteNotice(noticeNumber);

        return ResponseEntity.ok().build();
    }


    @PreAuth(Accessible.ADMIN)
    @PostMapping("/notice/modify/{noticeNumber}")
    public ResponseEntity<Void> modifyNotice(@PositiveOrZero @PathVariable(value = "noticeNumber") long noticeNumber,
                                             @RequestBody AdminNoticeRequest request) {
        adminUseCase.modifyNotice(noticeNumber, NoticeType.valueOf(request.getNoticeType()), request.getTitle(), request.getContent());

        return ResponseEntity.ok().build();
    }


    @PreAuth(Accessible.ADMIN)
    @GetMapping("/news/update")
    public ResponseEntity<Void> updateNewsLetter() {
        adminUseCase.forceNewsUpdate();

        return ResponseEntity.ok().build();
    }


    @PreAuth(Accessible.ADMIN)
    @PostMapping("/post/delete/{postId}")
    public ResponseEntity<Void> deletePost(@PositiveOrZero @PathVariable(value = "postId") long postId) {
        adminUseCase.deletePost(postId);

        return ResponseEntity.ok().build();
    }

}
