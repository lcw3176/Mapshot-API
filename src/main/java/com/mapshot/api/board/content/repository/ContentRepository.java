package com.mapshot.api.board.content.repository;

import com.mapshot.api.board.content.entity.ContentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContentRepository extends JpaRepository<ContentEntity, Long> {

    ContentEntity findFirstByOrderByIdDesc();

    List<ContentEntity> findTop10ByIdLessThanOrderByIdDesc(long id);
}
