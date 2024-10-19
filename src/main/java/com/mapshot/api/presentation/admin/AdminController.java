package com.mapshot.api.presentation.admin;

import com.mapshot.api.application.admin.AdminUseCase;
import com.mapshot.api.application.auth.Validation;
import com.mapshot.api.application.auth.annotation.PreAuth;
import com.mapshot.api.application.auth.enums.Accessible;
import com.mapshot.api.domain.notice.NoticeType;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@Validated
public class AdminController {

    private final AdminUseCase adminUseCase;
    private final Validation adminValidation;


    @PostMapping("/user/login")
    public ResponseEntity<Void> login(@RequestBody AdminUserRequest request, HttpServletResponse response) {
        adminUseCase.login(request.getNickname(), request.getPassword());

        // fixme
        // 호환성 때문에 일단 놔둠
        HttpHeaders authHeader = adminValidation.makeHeader();

        Cookie cookie = adminValidation.makeCookie();
        response.addCookie(cookie);

        return ResponseEntity.ok()
                .headers(authHeader)
                .build();
    }

    @PreAuth(Accessible.ADMIN)
    @PostMapping("/user/auth/refresh")
    public ResponseEntity<Void> refreshAuth(HttpServletResponse response) {
        HttpHeaders authHeader = adminValidation.makeHeader();

        Cookie cookie = adminValidation.makeCookie();
        response.addCookie(cookie);

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
    @GetMapping("/post/delete/{postId}")
    public ResponseEntity<Void> deletePost(@PositiveOrZero @PathVariable(value = "postId") long postId) {
        adminUseCase.deletePost(postId);

        return ResponseEntity.ok().build();
    }

}
