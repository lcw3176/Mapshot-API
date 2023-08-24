package com.mapshot.api.board.content.model;

import com.mapshot.api.board.content.consts.ContentConfig;
import com.mapshot.api.board.content.entity.ContentEntity;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContentRequest {

    @NotBlank
    @Length(max = ContentConfig.MAX_NICKNAME_LENGTH)
    private String nickname;

    @NotBlank
    @Length(max = ContentConfig.MAX_TITLE_LENGTH)
    private String title;

    @NotBlank
    @Length(max = ContentConfig.MAX_CONTENT_LENGTH)
    private String content;

    public ContentEntity toEntity() {

        return ContentEntity.builder()
                .title(title)
                .content(content)
                .commentCount(0L)
                .viewCount(0L)
                .nickname(nickname)
                .build();
    }
}
