package com.mapshot.api.domain.notice;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoticeRepository extends JpaRepository<NoticeEntity, Long> {
    List<NoticeEntity> findTop20ByIdLessThanOrderByIdDesc(long id);

    NoticeEntity findFirstByOrderByIdDesc();

}
