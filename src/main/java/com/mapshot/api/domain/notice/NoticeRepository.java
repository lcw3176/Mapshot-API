package com.mapshot.api.domain.notice;


import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<NoticeEntity, Long> {
    NoticeEntity findFirstByOrderByIdDesc();

}
