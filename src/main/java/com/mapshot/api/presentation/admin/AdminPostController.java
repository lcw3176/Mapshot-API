package com.mapshot.api.presentation.admin;

import com.mapshot.api.domain.admin.community.post.AdminPostService;
import com.mapshot.api.infra.auth.annotation.PreAuth;
import com.mapshot.api.infra.auth.enums.Accessible;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@CrossOrigin(originPatterns = {"https://*.kmapshot.com", "https://kmapshot.com"})
@Validated
public class AdminPostController {

    private final AdminPostService adminPostService;


    @PreAuth(Accessible.ADMIN)
    @GetMapping("/post/delete/{postNumber}")
    public ResponseEntity<Void> deletePost(@Positive @PathVariable(value = "postNumber") long postNumber) {
        adminPostService.deletePost(postNumber);

        return ResponseEntity.ok().build();
    }
}
