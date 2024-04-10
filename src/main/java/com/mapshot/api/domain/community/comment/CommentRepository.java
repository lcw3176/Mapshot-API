package com.mapshot.api.domain.community.comment;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    Page<CommentEntity> findAllByPostIdAndDeletedFalse(Pageable pageable, Long postId);
}
