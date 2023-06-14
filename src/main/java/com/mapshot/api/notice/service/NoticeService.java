package com.mapshot.api.notice.service;


import com.mapshot.api.notice.entity.NoticeEntity;
import com.mapshot.api.notice.model.PostDetailResponse;
import com.mapshot.api.notice.model.PostSummaryResponse;
import com.mapshot.api.notice.repository.NoticeRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepository;

    private static final String BAD_REQUEST = "[ERROR] 존재하지 않는 공지사항 접근";

    @Transactional(readOnly = true)
    public List<PostSummaryResponse> getMultiplePostsSummary(long startId) {

        if (startId == 0) {
            startId = noticeRepository.findFirstByOrderByIdDesc().getId() + 1;
        }

        List<NoticeEntity> noticeEntities = noticeRepository.findTop10ByIdLessThanOrderByIdDesc(startId);

        return noticeEntities.stream()
                .map(i -> PostSummaryResponse.builder()
                        .id(i.getId())
                        .noticeType(i.getNoticeType().getKorean())
                        .title(i.getTitle())
                        .createdDate(i.getCreatedDate())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PostDetailResponse getSinglePost(long id) {
        NoticeEntity noticeEntity = noticeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(BAD_REQUEST));

        return PostDetailResponse.builder()
                .id(noticeEntity.getId())
                .noticeType(noticeEntity.getNoticeType().getKorean())
                .title(noticeEntity.getTitle())
                .content(noticeEntity.getContent())
                .createdDate(noticeEntity.getCreatedDate())
                .build();
    }

}
