package com.mapshot.api.notice.repository;


import com.mapshot.api.notice.entity.NoticeEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<NoticeEntity, Long> {
    List<NoticeEntity> findTop10ByIdLessThanOrderByIdDesc(long id);

    NoticeEntity findFirstByOrderByIdDesc();

}
