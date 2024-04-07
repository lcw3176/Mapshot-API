package com.mapshot.api.domain.community.post;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PostDto {
    private long id;
    private String writer;
    private String title;
    private int commentCount;
    private LocalDateTime createdDate;
}
