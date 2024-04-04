package com.mapshot.api.domain.community.post;

import com.mapshot.api.infra.db.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "post")
public class PostEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String writer;

    @Column(length = PostConst.MAX_TITLE_LENGTH)
    private String title;

    @Column(length = PostConst.MAX_CONTENT_LENGTH)
    private String content;

    @Column
    private String password;

    @Column
    @ColumnDefault("0")
    private Integer commentCount;
}
