package com.mapshot.api.application.admin;

import com.mapshot.api.domain.community.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminPostUseCase {

    private final PostService postService;

    public void deletePost(long postId) {
        postService.deleteById(postId);
    }
}
