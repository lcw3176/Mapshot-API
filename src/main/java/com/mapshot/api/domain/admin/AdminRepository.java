package com.mapshot.api.domain.admin;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<AdminEntity, Long> {
    Optional<AdminEntity> findByNicknameAndPassword(String nickname, String password);
}
