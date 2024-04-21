package com.mapshot.api.domain.admin.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminUserRepository extends JpaRepository<AdminUserEntity, Long> {
    Optional<AdminUserEntity> findByNicknameAndPassword(String nickname, String password);
}
