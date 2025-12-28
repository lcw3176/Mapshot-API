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
public class Notice extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    @Enumerated(EnumType.STRING)
    private NoticeType noticeType;

    @Column(length = 2000)
    private String content;

    public void update(String title, NoticeType noticeType, String content) {
        this.title = title;
        this.noticeType = noticeType;
        this.content = content;
    }
}
