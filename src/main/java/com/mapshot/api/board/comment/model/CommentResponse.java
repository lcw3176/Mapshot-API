package com.mapshot.api.board.comment.model;

import com.mapshot.api.board.comment.entity.CommentEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponse {

    private String nickname;
    private String content;
    private Long referenceCommentId;
    private LocalDateTime createdDate;

    public static CommentResponse fromEntity(CommentEntity comment) {

        return CommentResponse.builder()
                .nickname(comment.getNickname())
                .content(comment.getContent())
                .referenceCommentId(comment.getReferenceCommentId())
                .createdDate(comment.getCreatedDate())
                .build();
    }
}
