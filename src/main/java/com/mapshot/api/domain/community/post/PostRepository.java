package com.mapshot.api.domain.community.post;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<PostEntity, Long> {

    PostEntity findFirstByOrderByIdDesc();

    Page<PostEntity> findAllByDeletedIsFalse(Pageable pageable);
}
