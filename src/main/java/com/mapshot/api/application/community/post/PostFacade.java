package com.mapshot.api.application.community.post;

import com.mapshot.api.domain.community.comment.CommentService;
import com.mapshot.api.domain.community.post.PostEntity;
import com.mapshot.api.domain.community.post.PostService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional
public class PostFacade {

    private final PostService postService;
    private final CommentService commentService;

    public PostListResponse getPostList(int pageNumber) {
        Page<PostEntity> pages = postService.getPostsByPageNumber(pageNumber);

        List<PostEntity> postEntities = pages.getContent();

        List<PostDto> postDtos = postEntities.stream()
                .map(i -> PostDto.builder()
                        .id(i.getId())
                        .title(i.getTitle())
                        .createdDate(i.getCreatedDate())
                        .writer(i.getWriter())
                        .commentCount(i.getCommentCount())
                        .build())
                .toList();

        return PostListResponse.builder()
                .posts(postDtos)
                .totalPage(pages.getTotalPages())
                .build();

    }

    public PostDetailResponse getPost(long postId) {
        PostEntity post = postService.getPostById(postId);

        return PostDetailResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .writer(post.getWriter())
                .content(post.getContent())
                .createdDate(post.getCreatedDate())
                .build();
    }

    public void save(String writer, String content, String title, String password) {
        postService.save(writer, content, title, password);
    }

    public void deleteIfOwner(long postId, String password) {
        postService.deleteIfOwner(postId, password);
        commentService.deleteByPostId(postId);
    }
}
