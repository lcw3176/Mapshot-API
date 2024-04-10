package com.mapshot.api.presentation.community.comment;

import com.mapshot.api.domain.community.comment.CommentResponse;
import com.mapshot.api.domain.community.comment.CommentService;
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
@RequestMapping("/comment")
@CrossOrigin(originPatterns = {"https://*.kmapshot.com", "https://kmapshot.com"})
@Validated
public class CommentController {

    private final CommentService commentService;

    @PreAuth(Accessible.EVERYONE)
    @GetMapping
    public ResponseEntity<CommentResponse> getPosts(@PositiveOrZero @RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                                    @PositiveOrZero @RequestParam(value = "postId") long postId) {
        CommentResponse responses = commentService.getComments(page, postId);

        return ResponseEntity.ok(responses);
    }


    @PreAuth(Accessible.EVERYONE)
    @PostMapping("/register")
    public ResponseEntity<Void> registerPost(@Valid @RequestBody CommentRegisterRequest request) {
        commentService.save(request.getWriter(), request.getContent(), request.getPostId(), request.getPassword());

        return ResponseEntity.ok().build();
    }

    @PreAuth(Accessible.EVERYONE)
    @GetMapping("/delete/{commentId}")
    public ResponseEntity<Void> deletePost(@Positive @PathVariable(value = "commentId") long commentId,
                                           @RequestParam("password") String password) {
        commentService.deleteIfOwner(commentId, password);

        return ResponseEntity.ok().build();
    }
}
