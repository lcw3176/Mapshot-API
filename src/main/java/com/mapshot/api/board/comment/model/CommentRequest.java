package com.mapshot.api.board.comment.model;

import com.mapshot.api.board.BoardConst;
import com.mapshot.api.board.comment.consts.CommentConst;
import com.mapshot.api.board.comment.entity.CommentEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentRequest {

    @NotBlank
    @Length(max = BoardConst.MAX_NICKNAME_LENGTH)
    private String nickname;

    @NotBlank
    @Length(max = CommentConst.MAX_COMMENT_LENGTH)
    private String content;

    @NotBlank
    @PositiveOrZero
    private Long referenceCommentId;

    @NotBlank
    @PositiveOrZero
    private Long contentId;

    public CommentEntity toEntity() {

        return CommentEntity.builder()
                .nickname(nickname)
                .content(content)
                .referenceCommentId(referenceCommentId)
                .contentId(contentId)
                .build();
    }
}
