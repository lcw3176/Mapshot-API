package com.mapshot.api.board.content.service;

import com.mapshot.api.board.content.entity.ContentEntity;
import com.mapshot.api.board.content.model.ContentDetailResponse;
import com.mapshot.api.board.content.model.ContentListResponse;
import com.mapshot.api.board.content.model.ContentRequest;
import com.mapshot.api.board.content.repository.ContentRepository;
import com.mapshot.api.common.exception.ApiException;
import com.mapshot.api.common.exception.status.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContentService {

    private final ContentRepository contentRepository;

    @Transactional(readOnly = true)
    public List<ContentListResponse> getContentList(long id) {
        if (id == 0) {
            id = contentRepository.findFirstByOrderByIdDesc().getId() + 1;
        }

        List<ContentEntity> contents = contentRepository.findTop10ByIdLessThanOrderByIdDesc(id);

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
