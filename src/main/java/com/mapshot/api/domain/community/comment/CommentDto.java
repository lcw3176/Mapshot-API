package com.mapshot.api.domain.community.comment;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CommentDto {
    private long id;
    private String writer;
    private String content;
    private LocalDateTime createdDate;
}
