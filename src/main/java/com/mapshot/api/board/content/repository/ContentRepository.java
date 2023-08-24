package com.mapshot.api.board.content.repository;

import com.mapshot.api.board.content.entity.ContentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentRepository extends JpaRepository<ContentEntity, Long> {

    Page<ContentEntity> findAll(Pageable pageable);
}
