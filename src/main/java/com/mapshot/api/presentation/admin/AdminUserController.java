package com.mapshot.api.presentation.admin;

import com.mapshot.api.domain.admin.user.AdminUserService;
import com.mapshot.api.infra.auth.annotation.PreAuth;
import com.mapshot.api.infra.auth.enums.Accessible;
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
public class AdminUserController {

    private final AdminUserService adminUserService;

    @PreAuth(Accessible.EVERYONE)
    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody AdminUserRequest request) {
        adminUserService.validateUser(request.getNickname(), request.getPassword());
        HttpHeaders authHeader = adminUserService.getAuthHeader();

        return ResponseEntity.ok()
                .headers(authHeader)
                .build();
    }

    @PreAuth(Accessible.ADMIN)
    @PostMapping("/auth/refresh")
    public ResponseEntity<Void> refreshAuth() {
        HttpHeaders authHeader = adminUserService.getAuthHeader();

        return ResponseEntity.ok()
                .headers(authHeader)
                .build();
    }

}
