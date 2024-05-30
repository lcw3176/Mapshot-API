package com.mapshot.api.application.community.comment;

import com.mapshot.api.domain.community.comment.CommentDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponse {

    private List<CommentDto> comments;
    private int totalPage;

}
