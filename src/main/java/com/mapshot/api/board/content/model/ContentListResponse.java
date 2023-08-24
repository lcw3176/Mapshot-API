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
public class ContentListResponse {

    private Long id;
    private String nickname;
    private String title;
    private Long commentCount;
    private Long viewCount;
    private LocalDateTime createdDate;

    public static ContentListResponse fromEntity(ContentEntity content) {

        return ContentListResponse.builder()
                .id(content.getId())
                .nickname(content.getNickname())
                .title(content.getTitle())
                .commentCount(content.getCommentCount())
                .viewCount(content.getViewCount())
                .createdDate(content.getCreatedDate())
                .build();
    }
}
