package com.mapshot.api.presentation.community.post;

import com.mapshot.api.domain.community.post.PostService;
import com.mapshot.api.infra.auth.annotation.PreAuth;
import com.mapshot.api.infra.auth.enums.Accessible;
import com.mapshot.api.presentation.community.post.model.PostDetailResponse;
import com.mapshot.api.presentation.community.post.model.PostListResponse;
import com.mapshot.api.presentation.community.post.model.PostRegisterRequest;
import jakarta.validation.constraints.Positive;
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

    @PreAuth(Accessible.EVERYONE)
    @GetMapping("/{id}")
    public ResponseEntity<List<PostListResponse>> getPosts(@PositiveOrZero @PathVariable(value = "id") long id) {
        List<PostListResponse> responses = postService.getPostListById(id);

        return ResponseEntity.ok(responses);
    }


    @PreAuth(Accessible.EVERYONE)
    @GetMapping("/detail/{id}")
    public ResponseEntity<PostDetailResponse> getSinglePost(@PositiveOrZero @PathVariable(value = "id") long id) {
        PostDetailResponse response = postService.getSinglePostById(id);

        return ResponseEntity.ok(response);
    }


    @PreAuth(Accessible.EVERYONE)
    @PostMapping("/register")
    public ResponseEntity<Void> registerPost(@RequestBody PostRegisterRequest request) {
        postService.save(request);

        return ResponseEntity.ok().build();
    }

    @PreAuth({Accessible.EVERYONE, Accessible.ADMIN})
    @GetMapping("/delete/{postNumber}")
    public ResponseEntity<Void> deletePost(@Positive @PathVariable(value = "postNumber") long postNumber,
                                           @RequestParam("password") String password) {
        postService.deleteIfOwner(postNumber, password);

        return ResponseEntity.ok().build();
    }
}
