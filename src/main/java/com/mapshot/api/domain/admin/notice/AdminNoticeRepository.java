package com.mapshot.api.domain.admin.notice;

import com.mapshot.api.domain.notice.NoticeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminNoticeRepository extends JpaRepository<NoticeEntity, Long> {
}
