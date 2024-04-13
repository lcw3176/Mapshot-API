package com.mapshot.api.domain.community.comment;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    Page<CommentEntity> findAllByPostIdAndDeletedFalse(Pageable pageable, Long postId);

    List<CommentEntity> findAllByPostIdAndDeletedFalse(Long postId);
}
