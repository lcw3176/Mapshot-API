package com.mapshot.api.board.comment.controller;

import com.mapshot.api.auth.annotation.PreAuth;
import com.mapshot.api.auth.enums.Accessible;
import com.mapshot.api.board.comment.model.CommentRequest;
import com.mapshot.api.board.comment.model.CommentResponse;
import com.mapshot.api.board.comment.service.CommentService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board/comment")
@CrossOrigin(originPatterns = {"https://*.kmapshot.com", "https://kmapshot.com"})
@Validated
public class CommentController {

    private final CommentService commentService;

    @PreAuth(Accessible.EVERYONE)
    @GetMapping
    public ResponseEntity<List<CommentResponse>> getComments(
            @Positive @RequestParam(value = "contentNumber") int contentNumber,
            @Positive @RequestParam(value = "pageNumber") int pageNumber) {

        List<CommentResponse> contentList = commentService.getComments(contentNumber, pageNumber);

        return ResponseEntity.ok()
                .body(contentList);
    }

    @PreAuth(Accessible.EVERYONE)
    @PostMapping("/register")
    public ResponseEntity<Void> registerContent(@RequestBody CommentRequest request) {
        commentService.save(request);

        return ResponseEntity.ok().build();
    }
}
