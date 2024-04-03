package com.mapshot.api.domain.community.post;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<PostEntity, Long> {

    PostEntity findFirstByOrderByIdDesc();
    
    List<PostEntity> findTop10ByIdLessThanOrderByIdDesc(long id);
}
