package com.joebrooks.mapshot.repository.notice;

import com.joebrooks.mapshot.repository.BaseTimeEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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

    @Column(name = "content", length = 2000)
    private String content;

    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeNoticeType(NoticeType noticeType) {
        this.noticeType = noticeType;
    }

    public void changeContent(String content) {
        this.content = content;
    }

}
