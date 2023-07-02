package com.mapshot.api.admin.controller;

import com.mapshot.api.admin.model.AdminRequest;
import com.mapshot.api.admin.service.AdminService;
import com.mapshot.api.auth.validation.Accessible;
import com.mapshot.api.auth.validation.annotation.PreAuth;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@CrossOrigin(originPatterns = {"https://*.kmapshot.com", "https://kmapshot.com"})
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
    @PostMapping("/auth/refresh")
    public ResponseEntity<Void> refreshAuth() {
        MultiValueMap<String, String> authHeader = adminService.makeToken();

        return ResponseEntity.ok()
                .headers((httpHeaders) -> httpHeaders.addAll(authHeader))
                .build();
    }
}
