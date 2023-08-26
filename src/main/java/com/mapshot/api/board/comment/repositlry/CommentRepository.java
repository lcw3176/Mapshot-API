package com.mapshot.api.board.comment.repositlry;

import com.mapshot.api.board.comment.entity.CommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    Page<CommentEntity> findAllByContentId(Long contentId, Pageable pageable);
}
