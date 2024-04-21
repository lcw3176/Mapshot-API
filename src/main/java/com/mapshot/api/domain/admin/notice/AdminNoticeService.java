package com.mapshot.api.domain.admin.notice;

import com.mapshot.api.domain.notice.NoticeEntity;
import com.mapshot.api.domain.notice.NoticeType;
import com.mapshot.api.infra.exception.ApiException;
import com.mapshot.api.infra.exception.status.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminNoticeService {

    private final AdminNoticeRepository adminNoticeRepository;

    @Transactional
    public long saveNotice(NoticeType type, String title, String content) {

        return adminNoticeRepository.save(NoticeEntity.builder()
                        .noticeType(type)
                        .title(title)
                        .content(content)
                        .build())
                .getId();
    }

    @Transactional
    public long modifyNotice(long id, NoticeType type, String title, String content) {

        NoticeEntity noticeEntity = adminNoticeRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.NO_SUCH_NOTICE));

        noticeEntity.update(title, type, content);

        return adminNoticeRepository.save(noticeEntity).getId();
    }


    @Transactional
    public void deleteNotice(long id) {
        NoticeEntity noticeEntity = adminNoticeRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.NO_SUCH_NOTICE));

        adminNoticeRepository.delete(noticeEntity);
    }


}
