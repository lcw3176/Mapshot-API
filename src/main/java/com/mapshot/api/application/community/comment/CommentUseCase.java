package com.mapshot.api.application.community.comment;

import com.mapshot.api.domain.community.comment.CommentDto;
import com.mapshot.api.domain.community.comment.CommentEntity;
import com.mapshot.api.domain.community.comment.CommentService;
import com.mapshot.api.domain.community.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CommentUseCase {

    private final CommentService commentService;
    private final PostService postService;

    public CommentResponse getComments(int pageNumber, long postId) {

        Page<CommentEntity> pages = commentService.findAllByPostId(pageNumber, postId);
        List<CommentEntity> commentEntities = pages.getContent();

        List<CommentDto> commentDtos = commentEntities.stream()
                .map(i -> CommentDto.builder()
                        .id(i.getId())
                        .createdDate(i.getCreatedDate())
                        .writer(i.getWriter())
                        .content(i.getContent())
                        .build())
                .toList();

        return CommentResponse.builder()
                .comments(commentDtos)
                .totalPage(pages.getTotalPages())
                .build();
    }


    public long save(String writer, String content, long postId, String password) {

        long commentId = commentService.save(writer, content, postId, password);
        postService.increaseCommentCount(postId);

        return commentId;
    }

    public void deleteIfOwner(long commentId, String password) {

        CommentEntity comment = commentService.findById(commentId);

        commentService.deleteIfOwner(comment.getId(), password);
        postService.decreaseCommentCount(comment.getPostId());
    }
}
