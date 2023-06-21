package com.mapshot.api.admin.controller;

import com.mapshot.api.admin.model.AdminRequest;
import com.mapshot.api.admin.service.AdminService;
import com.mapshot.api.common.validation.Accessible;
import com.mapshot.api.common.validation.PreAuth;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    @PreAuth(Accessible.EVERYONE)
    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody AdminRequest request) {
        MultiValueMap<String, String> authHeader = adminService.login(request);

        return ResponseEntity.ok()
                .headers((httpHeaders) -> httpHeaders.addAll(authHeader))
                .build();
    }

    @PreAuth(Accessible.ADMIN)
    @PostMapping("/token/refresh")
    public ResponseEntity<Void> refreshToken() {
        MultiValueMap<String, String> authHeader = adminService.makeToken();

        return ResponseEntity.ok()
                .headers((httpHeaders) -> httpHeaders.addAll(authHeader))
                .build();
    }
}
