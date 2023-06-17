package com.mapshot.api.notice.service;


import com.mapshot.api.common.exception.ApiException;
import com.mapshot.api.common.exception.status.ErrorCode;
import com.mapshot.api.notice.entity.NoticeEntity;
import com.mapshot.api.notice.enums.NoticeType;
import com.mapshot.api.notice.model.NoticeDetailResponse;
import com.mapshot.api.notice.model.NoticeRequest;
import com.mapshot.api.notice.model.NoticeSummaryResponse;
import com.mapshot.api.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    @Transactional(readOnly = true)
    public List<NoticeSummaryResponse> getMultiplePostsSummary(long startId) {

        if (startId == 0) {
            startId = noticeRepository.findFirstByOrderByIdDesc().getId() + 1;
        }

        List<NoticeEntity> noticeEntities = noticeRepository.findTop10ByIdLessThanOrderByIdDesc(startId);

        return noticeEntities.stream()
                .map(NoticeSummaryResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public NoticeDetailResponse getSinglePost(long id) {
        NoticeEntity noticeEntity = noticeRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.NO_SUCH_NOTICE));

        return NoticeDetailResponse.fromEntity(noticeEntity);
    }

    @Transactional
    public long save(NoticeRequest request) {

        return noticeRepository.save(request.toEntity()).getId();
    }

    @Transactional
    public long update(NoticeRequest request) {

        NoticeEntity noticeEntity = noticeRepository.findById(request.getId())
                .orElseThrow(() -> new ApiException(ErrorCode.NO_SUCH_NOTICE));

        noticeEntity.update(request.getTitle(), NoticeType.valueOf(request.getNoticeType()), request.getContent());

        return noticeRepository.save(noticeEntity).getId();
    }


}
