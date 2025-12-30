package com.mapshot.api.presentation.admin;


import com.mapshot.api.application.admin.AdminUserService;
import com.mapshot.api.domain.notice.NoticeType;
import com.mapshot.api.infra.auth.Accessible;
import com.mapshot.api.infra.auth.PreAuth;
import com.mapshot.api.infra.auth.Validator;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@Validated
public class AdminController {

    private final AdminUserService adminUserService;

    @Value("${jwt.admin.header}")
    private String ADMIN_SESSION;

    @PostMapping("/user/login")
    public ResponseEntity<Void> login(@RequestBody AdminUserRequest request, HttpSession session) {
        adminUserService.login(request.getNickname(), request.getPassword());

        session.setAttribute(ADMIN_SESSION, true);

        return ResponseEntity.ok()
                .build();
    }

    @PreAuth(Accessible.ADMIN)
    @PostMapping("/user/auth/refresh")
    public ResponseEntity<Void> refreshAuth(HttpSession session) {

        session.setAttribute(ADMIN_SESSION, true);

        return ResponseEntity.ok()
                .build();
    }


    @PreAuth(Accessible.ADMIN)
    @PostMapping("/notice/register")
    public ResponseEntity<Void> registerNotice(@RequestBody AdminNoticeRequest request) {
        adminUserService.saveNotice(NoticeType.valueOf(request.getNoticeType()), request.getTitle(), request.getContent());

        return ResponseEntity.ok().build();
    }

    @PreAuth(Accessible.ADMIN)
    @GetMapping("/notice/delete/{noticeNumber}")
    public ResponseEntity<Void> deleteNotice(@PositiveOrZero @PathVariable(value = "noticeNumber") long noticeNumber) {
        adminUserService.deleteNotice(noticeNumber);

        return ResponseEntity.ok().build();
    }


    @PreAuth(Accessible.ADMIN)
    @PostMapping("/notice/modify/{noticeNumber}")
    public ResponseEntity<Void> modifyNotice(@PositiveOrZero @PathVariable(value = "noticeNumber") long noticeNumber,
                                             @RequestBody AdminNoticeRequest request) {
        adminUserService.modifyNotice(noticeNumber, NoticeType.valueOf(request.getNoticeType()), request.getTitle(), request.getContent());

        return ResponseEntity.ok().build();
    }


}
