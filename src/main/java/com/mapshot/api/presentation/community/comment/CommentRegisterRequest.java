package com.mapshot.api.presentation.community.comment;

import com.mapshot.api.domain.community.comment.CommentConst;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentRegisterRequest {

    @PositiveOrZero
    private Long postId;

    @NotBlank
    private String writer;

    @NotBlank
    @Size(max = CommentConst.MAX_CONTENT_LENGTH)
    private String content;

    @NotBlank
    private String password;

}
