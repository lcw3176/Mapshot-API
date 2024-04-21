package com.mapshot.api.domain.admin.community.post;

import com.mapshot.api.domain.community.post.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminPostRepository extends JpaRepository<PostEntity, Long> {
}
