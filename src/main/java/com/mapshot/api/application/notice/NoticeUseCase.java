package com.mapshot.api.application.notice;

import com.mapshot.api.domain.notice.NoticeEntity;
import com.mapshot.api.domain.notice.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NoticeUseCase {

    private final NoticeService noticeService;

    public NoticeListResponse getNoticeList(int pageNumber) {
        Page<NoticeEntity> pages = noticeService.findByPageNumber(pageNumber);

        List<NoticeDto> noticeDtos = pages.stream()
                .map(NoticeDto::fromEntity)
                .toList();

        return NoticeListResponse.builder()
                .notices(noticeDtos)
                .totalPage(pages.getTotalPages())
                .build();
    }

    public NoticeDetailResponse getNotice(long noticeId) {
        NoticeEntity notice = noticeService.findById(noticeId);

        return NoticeDetailResponse.builder()
                .id(notice.getId())
                .noticeType(notice.getNoticeType().getKorean())
                .title(notice.getTitle())
                .content(notice.getContent())
                .createdDate(notice.getCreatedDate())
                .build();
    }
}
