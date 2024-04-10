package com.mapshot.api.domain.community.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponse {

    private List<CommentDto> comments = new ArrayList<>();
    private int totalPage;

}
