package com.mapshot.api.admin.controller;

import com.mapshot.api.admin.model.AdminRequest;
import com.mapshot.api.common.token.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {


    @PostMapping("/login")
    public ResponseEntity<Void> login(AdminRequest request) {


        return ResponseEntity.ok()
                .header(JwtProvider.HEADER_NAME, JwtProvider.generate(60 * 60))
                .build();
    }
}
