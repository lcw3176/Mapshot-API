package com.mapshot.api.domain.notice;

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
@Entity(name = "notice")
public class NoticeEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private NoticeType noticeType;

    @Column(name = "content", length = NoticeConst.MAX_CONTENT_LENGTH)
    private String content;

    public void update(String title, NoticeType noticeType, String content) {
        this.title = title;
        this.noticeType = noticeType;
        this.content = content;
    }
}
