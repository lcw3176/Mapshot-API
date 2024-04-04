package com.mapshot.api.presentation.community.post.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDetailResponse {

    private long id;
    private String writer;
    private String title;
    private String content;
    private LocalDateTime createdDate;

}
