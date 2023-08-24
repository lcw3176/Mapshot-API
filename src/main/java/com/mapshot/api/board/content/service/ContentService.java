package com.mapshot.api.board.content.service;

import com.mapshot.api.board.content.entity.ContentEntity;
import com.mapshot.api.board.content.model.ContentDetailResponse;
import com.mapshot.api.board.content.model.ContentListResponse;
import com.mapshot.api.board.content.model.ContentRequest;
import com.mapshot.api.board.content.repository.ContentRepository;
import com.mapshot.api.common.exception.ApiException;
import com.mapshot.api.common.exception.status.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContentService {

    private final ContentRepository contentRepository;
    private static final int DEFAULT_PAGE_COUNT = 10;

    @Transactional(readOnly = true)
    public List<ContentListResponse> getContentList(int pageNumber) {
        pageNumber -= 1;

        if (pageNumber < 0) {
            throw new ApiException(ErrorCode.NO_SUCH_CONTENT);
        }

        Pageable pageable = PageRequest.of(pageNumber, DEFAULT_PAGE_COUNT, Sort.by(Sort.Direction.DESC, "id"));

        Page<ContentEntity> contents = contentRepository.findAll(pageable);

        return contents.stream()
                .map(ContentListResponse::fromEntity)
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public ContentDetailResponse getSingleContent(long id) {
        ContentEntity content = contentRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.NO_SUCH_CONTENT));

        return ContentDetailResponse.fromEntity(content);
    }

    @Transactional
    public long save(ContentRequest request) {

        return contentRepository.save(request.toEntity()).getId();
    }
}
