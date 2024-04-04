package com.mapshot.api.presentation.admin;

import com.mapshot.api.domain.admin.AdminService;
import com.mapshot.api.infra.auth.annotation.PreAuth;
import com.mapshot.api.infra.auth.enums.Accessible;
import com.mapshot.api.presentation.admin.model.AdminRequest;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
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
        adminService.validateUser(request);
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
    @GetMapping("/post/delete/{postNumber}")
    public ResponseEntity<Void> deletePost(@Positive @PathVariable(value = "postNumber") long postNumber) {
        adminService.deletePost(postNumber);

        return ResponseEntity.ok().build();
    }
}
