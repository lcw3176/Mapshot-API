package com.mapshot.api.presentation.community.post;

import com.mapshot.api.domain.community.post.PostDetailResponse;
import com.mapshot.api.domain.community.post.PostListResponse;
import com.mapshot.api.domain.community.post.PostService;
import com.mapshot.api.infra.auth.annotation.PreAuth;
import com.mapshot.api.infra.auth.enums.Accessible;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
@CrossOrigin(originPatterns = {"https://*.kmapshot.com", "https://kmapshot.com"})
@Validated
public class PostController {

    private final PostService postService;

    @PreAuth(Accessible.EVERYONE)
    @GetMapping
    public ResponseEntity<PostListResponse> getPosts(@PositiveOrZero @RequestParam(value = "page", defaultValue = "0", required = false) int page) {
        PostListResponse responses = postService.getPostListByPageNumber(page);

        return ResponseEntity.ok(responses);
    }


    @PreAuth(Accessible.EVERYONE)
    @GetMapping("/{id}")
    public ResponseEntity<PostDetailResponse> getSinglePost(@Positive @PathVariable(value = "id") long id) {
        PostDetailResponse response = postService.getSinglePostById(id);

        return ResponseEntity.ok(response);
    }


    @PreAuth(Accessible.EVERYONE)
    @PostMapping("/register")
    public ResponseEntity<Void> registerPost(@Valid @RequestBody PostRegisterRequest request) {
        postService.save(request.getWriter(), request.getContent(), request.getTitle(), request.getPassword());

        return ResponseEntity.ok().build();
    }

    @PreAuth(Accessible.EVERYONE)
    @GetMapping("/delete/{postNumber}")
    public ResponseEntity<Void> deletePost(@Positive @PathVariable(value = "postNumber") long postNumber,
                                           @RequestParam("password") String password) {
        postService.deleteIfOwner(postNumber, password);

        return ResponseEntity.ok().build();
    }
}
