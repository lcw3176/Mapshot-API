package com.mapshot.api.presentation.community.post;

import com.mapshot.api.application.community.post.PostDetailResponse;
import com.mapshot.api.application.community.post.PostListResponse;
import com.mapshot.api.application.community.post.PostUseCase;
import com.mapshot.api.application.auth.annotation.PreAuth;
import com.mapshot.api.application.auth.enums.Accessible;
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
@Validated
public class PostController {

    private final PostUseCase postUseCase;

    @PreAuth(Accessible.EVERYONE)
    @GetMapping
    public ResponseEntity<PostListResponse> getPosts(@PositiveOrZero @RequestParam(value = "page", defaultValue = "0", required = false) int page) {
        PostListResponse responses = postUseCase.getPostList(page);

        return ResponseEntity.ok(responses);
    }


    @PreAuth(Accessible.EVERYONE)
    @GetMapping("/{id}")
    public ResponseEntity<PostDetailResponse> getSinglePost(@Positive @PathVariable(value = "id") long id) {
        PostDetailResponse response = postUseCase.getPost(id);

        return ResponseEntity.ok(response);
    }


    @PreAuth(Accessible.EVERYONE)
    @PostMapping("/register")
    public ResponseEntity<Void> registerPost(@Valid @RequestBody PostRegisterRequest request) {
        postUseCase.save(request.getWriter(), request.getContent(), request.getTitle(), request.getPassword());

        return ResponseEntity.ok().build();
    }

    @PreAuth(Accessible.EVERYONE)
    @GetMapping("/delete/{postNumber}")
    public ResponseEntity<Void> deletePost(@Positive @PathVariable(value = "postNumber") long postNumber,
                                           @RequestParam("password") String password) {
        postUseCase.deleteIfOwner(postNumber, password);

        return ResponseEntity.ok().build();
    }
}
