package com.mapshot.api.domain.community.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostListResponse {

    private long id;
    private String writer;
    private String title;
    private int commentCount;
    private LocalDateTime createdDate;

}
