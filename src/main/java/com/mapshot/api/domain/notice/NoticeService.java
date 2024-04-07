package com.mapshot.api.domain.notice;


import com.mapshot.api.infra.exception.ApiException;
import com.mapshot.api.infra.exception.status.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    @Value("${notice.post.page_size}")
    private int PAGE_SIZE;

    @Transactional(readOnly = true)
    public NoticeListResponse getNoticeByPageNumber(int page) {

        if (page <= 0) {
            page = 1;
        }

        Pageable pageable = PageRequest.of(--page, PAGE_SIZE, Sort.by(Sort.Direction.DESC, "id"));
        Page<NoticeEntity> pages = noticeRepository.findAll(pageable);

        List<NoticeDto> noticeDtos = pages.stream()
                .map(NoticeDto::fromEntity)
                .toList();

        return NoticeListResponse.builder()
                .notices(noticeDtos)
                .totalPage(pages.getTotalPages())
                .build();
    }

    @Transactional(readOnly = true)
    public NoticeDetailResponse getSinglePost(long id) {
        NoticeEntity noticeEntity = noticeRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.NO_SUCH_NOTICE));

        return NoticeDetailResponse.fromEntity(noticeEntity);
    }

}
