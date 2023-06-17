package com.mapshot.api.admin.controller;

import com.mapshot.api.admin.model.AdminRequest;
import com.mapshot.api.admin.service.AdminService;
import com.mapshot.api.common.annotation.PreAuth;
import com.mapshot.api.common.enums.AuthType;
import com.mapshot.api.common.token.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/login")
    public ResponseEntity<Void> login(AdminRequest request) {
        MultiValueMap<String, String> authHeader = adminService.login(request);

        return ResponseEntity.ok()
                .headers((httpHeaders) -> httpHeaders.addAll(authHeader))
                .build();
    }

    @PreAuth(AuthType.ADMIN)
    @PostMapping("/token/refresh")
    public ResponseEntity<Void> refreshToken(HttpHeaders headers) {
        headers.remove(JwtUtil.ADMIN_HEADER_NAME);
        MultiValueMap<String, String> authHeader = adminService.makeToken();

        return ResponseEntity.ok()
                .headers((httpHeaders) -> httpHeaders.addAll(authHeader))
                .build();
    }


    @PreAuth(AuthType.ADMIN)
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpHeaders httpHeaders) {
        httpHeaders.remove(JwtUtil.ADMIN_HEADER_NAME);

        return ResponseEntity.ok().build();
    }
}
