package com.mapshot.api.application.community.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostListResponse {

    private List<PostDto> posts;
    private int totalPage;

}
