package com.joebrooks.mapshotapi.repository.notice;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepository;

    private static final String BAD_REQUEST = "[ERROR] 존재하지 않는 공지사항 접근";

    @Transactional
    public void addPost(NoticeEntity noticeEntity) {
        noticeRepository.save(noticeEntity);
    }

    @Transactional(readOnly = true)
    public List<NoticeEntity> getPosts(long id) {
        return noticeRepository.findTop10ByIdLessThanEqualOrderByIdDesc(id);
    }

    @Transactional(readOnly = true)
    public NoticeEntity getPost(long id) {
        return noticeRepository.findById(id).orElseThrow(() -> {
            throw new IllegalArgumentException(BAD_REQUEST);
        });
    }

    @Transactional
    public void removePost(long id) {
        noticeRepository.deleteById(id);
    }

    @Transactional
    public void editPost(NoticeEntity noticeEntityToChange) {

        NoticeEntity noticeEntity = noticeRepository.findById(noticeEntityToChange.getId())
                .orElseThrow(() -> {
                    throw new IllegalArgumentException(BAD_REQUEST);
                });

        noticeEntity.changeNoticeType(noticeEntityToChange.getNoticeType());
        noticeEntity.changeContent(noticeEntityToChange.getContent());
        noticeEntity.changeTitle(noticeEntityToChange.getTitle());
    }
}
