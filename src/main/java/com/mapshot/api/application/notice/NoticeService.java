package com.mapshot.api.application.notice;


import com.mapshot.api.domain.notice.Notice;
import com.mapshot.api.domain.notice.NoticeRepository;
import com.mapshot.api.domain.notice.NoticeType;
import com.mapshot.api.infra.exception.ApiException;
import com.mapshot.api.infra.exception.status.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    @Value("${notice.post.page_size}")
    private int PAGE_SIZE;


    public NoticeListResponse getList(int pageNumber){
        if (pageNumber <= 0) {
            pageNumber = 1;
        }

        Pageable pageable = PageRequest.of(--pageNumber, PAGE_SIZE, Sort.by(Sort.Direction.DESC, "id"));
        Page<Notice> pages = noticeRepository.findAll(pageable);

        List<NoticeDto> noticeDtos = pages.stream()
                .map(NoticeDto::fromEntity)
                .toList();

        return NoticeListResponse.builder()
                .notices(noticeDtos)
                .totalPage(pages.getTotalPages())
                .build();
    }

    public NoticeDetailResponse getDetail(long noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new ApiException(ErrorCode.NO_SUCH_NOTICE));

        return NoticeDetailResponse.builder()
                .id(notice.getId())
                .noticeType(notice.getNoticeType().getKorean())
                .title(notice.getTitle())
                .content(notice.getContent())
                .createdDate(notice.getCreatedDate())
                .build();
    }

}
