package com.mapshot.api.domain.admin;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminUserRepository extends JpaRepository<AdminUserEntity, Long> {

    Optional<AdminUserEntity> findByUserNameAndPassword(String userName, String password);

}
