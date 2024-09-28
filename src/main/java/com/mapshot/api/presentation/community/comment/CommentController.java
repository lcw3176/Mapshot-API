package com.mapshot.api.presentation.community.comment;

import com.mapshot.api.application.community.comment.CommentResponse;
import com.mapshot.api.application.community.comment.CommentUseCase;
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
@RequestMapping("/comment")
@Validated
public class CommentController {

    private final CommentUseCase commentUseCase;

    @GetMapping
    public ResponseEntity<CommentResponse> getPosts(@PositiveOrZero @RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                                    @PositiveOrZero @RequestParam(value = "postId") long postId) {

        CommentResponse responses = commentUseCase.getComments(page, postId);

        return ResponseEntity.ok(responses);
    }


    @PostMapping("/register")
    public ResponseEntity<Void> registerPost(@Valid @RequestBody CommentRegisterRequest request) {
        commentUseCase.save(request.getWriter(), request.getContent(), request.getPostId(), request.getPassword());

        return ResponseEntity.ok().build();
    }

    @GetMapping("/delete/{commentId}")
    public ResponseEntity<Void> deletePost(@Positive @PathVariable(value = "commentId") long commentId,
                                           @RequestParam("password") String password) {
        commentUseCase.deleteIfOwner(commentId, password);

        return ResponseEntity.ok().build();
    }
}
