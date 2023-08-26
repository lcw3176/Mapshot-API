package com.mapshot.api.board.comment.entity;

import com.mapshot.api.board.BoardConst;
import com.mapshot.api.board.comment.consts.CommentConst;
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
@Entity(name = "comment")
public class CommentEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = BoardConst.MAX_NICKNAME_LENGTH)
    private String nickname;

    @Column(length = CommentConst.MAX_COMMENT_LENGTH)
    private String content;

    private Long contentId;

    private Long referenceCommentId;
    
}
