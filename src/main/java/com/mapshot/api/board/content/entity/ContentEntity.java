package com.mapshot.api.board.content.entity;

import com.mapshot.api.board.content.consts.ContentConfig;
import com.mapshot.api.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "content")
public class ContentEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickname;

    @Column(length = ContentConfig.MAX_TITLE_LENGTH)
    private String title;

    @Column(length = ContentConfig.MAX_CONTENT_LENGTH)
    private String content;

    @Column
    private Long commentCount;

    @Column
    private Long viewCount;


}
