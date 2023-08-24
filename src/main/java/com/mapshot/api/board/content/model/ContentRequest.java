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

    private String writerIpAddress;

    @NotBlank
    private String title;

    @NotBlank
    @Length(max = ContentConfig.MAX_CONTENT_LENGTH)
    private String content;

    public void setIp(String writerIpAddress) {
        this.writerIpAddress = writerIpAddress;
    }


    public ContentEntity toEntity() {

        return ContentEntity.builder()
                .title(title)
                .content(content)
                .commentCount(0L)
                .viewCount(0L)
                .writerIpAddress(writerIpAddress)
                .build();
    }
}
