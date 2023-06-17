package com.mapshot.api.notice.entity;

import com.mapshot.api.common.entity.BaseTimeEntity;
import com.mapshot.api.notice.enums.NoticeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    @Column(name = "content", length = 2000)
    private String content;

    public void update(String title, NoticeType noticeType, String content) {
        this.title = title;
        this.noticeType = noticeType;
        this.content = content;
    }
}
