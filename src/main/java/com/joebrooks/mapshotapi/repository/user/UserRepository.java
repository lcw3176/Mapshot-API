package com.joebrooks.mapshotapi.repository.user;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    boolean existsByUserNameAndPassword(String id, String pw);

    Optional<UserEntity> findByUserName(String id);

}