package com.mapshot.api.domain.notice;


import com.mapshot.api.infra.exception.ApiException;
import com.mapshot.api.infra.exception.status.ErrorCode;
import com.mapshot.api.presentation.notice.model.NoticeDetailResponse;
import com.mapshot.api.presentation.notice.model.NoticeListResponse;
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
    public List<NoticeListResponse> getNoticeList(long startId) {

        if (startId == 0) {
            startId = noticeRepository.findFirstByOrderByIdDesc().getId() + 1;
        }

        List<NoticeEntity> noticeEntities = noticeRepository.findTop20ByIdLessThanOrderByIdDesc(startId);

        return noticeEntities.stream()
                .map(NoticeListResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public NoticeDetailResponse getSinglePost(long id) {
        NoticeEntity noticeEntity = noticeRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.NO_SUCH_NOTICE));

        return NoticeDetailResponse.fromEntity(noticeEntity);
    }

}
