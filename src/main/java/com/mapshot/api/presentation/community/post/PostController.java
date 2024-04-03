package com.mapshot.api.presentation.community.post;

import com.mapshot.api.domain.community.post.PostService;
import com.mapshot.api.presentation.community.post.model.PostListResponse;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
@CrossOrigin(originPatterns = {"https://*.kmapshot.com", "https://kmapshot.com"})
@Validated
public class PostController {

    private final PostService postService;

    @GetMapping("/{id}")
    public ResponseEntity<List<PostListResponse>> getPosts(@PositiveOrZero @PathVariable(value = "id") long id) {
        List<PostListResponse> responses = postService.getPostListById(id);

        return ResponseEntity.ok(responses);
    }
}
