package com.mapshot.api.board.content.model;

import com.mapshot.api.board.content.entity.ContentEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContentDetailResponse {

    private Long id;
    private String writerIpAddress;
    private String content;
    private String title;
    private Long commentCount;
    private Long viewCount;
    private LocalDateTime createdDate;

    public static ContentDetailResponse fromEntity(ContentEntity content) {

        return ContentDetailResponse.builder()
                .id(content.getId())
                .writerIpAddress(content.getWriterIpAddress())
                .content(content.getContent())
                .title(content.getTitle())
                .commentCount(content.getCommentCount())
                .viewCount(content.getViewCount())
                .createdDate(content.getCreatedDate())
                .build();
    }
}
