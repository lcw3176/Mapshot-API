package com.mapshot.api.domain.community.comment;

import com.mapshot.api.infra.db.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "comment")
public class CommentEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long postId;

    @Column
    private String writer;

    @Column(length = CommentConst.MAX_CONTENT_LENGTH)
    private String content;

    @Column
    private String password;

    @Column
    private Boolean deleted;

}
