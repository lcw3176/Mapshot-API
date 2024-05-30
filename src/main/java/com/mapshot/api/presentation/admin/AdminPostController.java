package com.mapshot.api.presentation.admin;

import com.mapshot.api.application.admin.AdminPostUseCase;
import com.mapshot.api.infra.auth.annotation.PreAuth;
import com.mapshot.api.infra.auth.enums.Accessible;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/post")
@CrossOrigin(originPatterns = {"https://*.kmapshot.com", "https://kmapshot.com"})
@Validated
public class AdminPostController {

    private final AdminPostUseCase adminPostUseCase;


    @PreAuth(Accessible.ADMIN)
    @GetMapping("/delete/{postNumber}")
    public ResponseEntity<Void> deletePost(@Positive @PathVariable(value = "postNumber") long postNumber) {
        adminPostUseCase.deletePost(postNumber);

        return ResponseEntity.ok().build();
    }
}
