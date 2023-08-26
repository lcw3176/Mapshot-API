package com.mapshot.api.board.comment.service;

import com.mapshot.api.board.comment.entity.CommentEntity;
import com.mapshot.api.board.comment.model.CommentRequest;
import com.mapshot.api.board.comment.model.CommentResponse;
import com.mapshot.api.board.comment.repositlry.CommentRepository;
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
public class CommentService {

    private final CommentRepository commentRepository;
    private static final int DEFAULT_PAGE_COUNT = 10;

    @Transactional(readOnly = true)
    public List<CommentResponse> getComments(long contentNumber, int pageNumber) {
        pageNumber -= 1;

        if (pageNumber < 0) {
            throw new ApiException(ErrorCode.NO_SUCH_COMMENT);
        }

        Pageable pageable = PageRequest.of(pageNumber, DEFAULT_PAGE_COUNT, Sort.by(Sort.Direction.DESC, "id"));

        Page<CommentEntity> comments = commentRepository.findAllByContentId(contentNumber, pageable);

        return comments.stream()
                .map(CommentResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public long save(CommentRequest request) {

        return commentRepository.save(request.toEntity()).getId();
    }
}
