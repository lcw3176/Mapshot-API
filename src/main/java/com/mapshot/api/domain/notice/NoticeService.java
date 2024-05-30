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

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    @Value("${notice.post.page_size}")
    private int PAGE_SIZE;

    @Transactional(readOnly = true)
    public Page<NoticeEntity> findByPageNumber(int page) {

        if (page <= 0) {
            page = 1;
        }

        Pageable pageable = PageRequest.of(--page, PAGE_SIZE, Sort.by(Sort.Direction.DESC, "id"));
        return noticeRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public NoticeEntity findById(long id) {

        return noticeRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.NO_SUCH_NOTICE));
    }


    @Transactional
    public long save(NoticeType type, String title, String content) {

        return noticeRepository.save(NoticeEntity.builder()
                        .noticeType(type)
                        .title(title)
                        .content(content)
                        .build())
                .getId();
    }

    @Transactional
    public long update(long id, NoticeType type, String title, String content) {

        NoticeEntity noticeEntity = noticeRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.NO_SUCH_NOTICE));

        noticeEntity.update(title, type, content);

        return noticeRepository.save(noticeEntity).getId();
    }


    @Transactional
    public void delete(long id) {
        NoticeEntity noticeEntity = noticeRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.NO_SUCH_NOTICE));

        noticeRepository.delete(noticeEntity);
    }

}
