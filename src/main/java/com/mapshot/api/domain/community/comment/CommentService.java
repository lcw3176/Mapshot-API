package com.mapshot.api.domain.community.comment;


import com.mapshot.api.infra.util.EncryptUtil;
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
public class CommentService {

    private final CommentRepository commentRepository;

    @Value("${community.comment.page_size}")
    private int PAGE_SIZE;

    @Transactional(readOnly = true)
    public Page<CommentEntity> findAllByPostId(int pageNumber, long postId) {

        if (pageNumber <= 0) {
            pageNumber = 1;
        }

        Pageable pageable = PageRequest.of(--pageNumber, PAGE_SIZE, Sort.by(Sort.Direction.ASC, "id"));

        return commentRepository.findAllByPostIdAndDeletedFalse(pageable, postId);
    }

    @Transactional
    public CommentEntity findById(long commentId) {

        return commentRepository.findById(commentId)
                .orElseThrow(() -> new ApiException(ErrorCode.NO_SUCH_POST));
    }

    @Transactional
    public long save(String writer, String content, long postId, String password) {

        return commentRepository.save(CommentEntity.builder()
                        .writer(writer)
                        .content(content)
                        .password(EncryptUtil.encrypt(password))
                        .deleted(false)
                        .postId(postId)
                        .build())
                .getId();
    }


    @Transactional
    public void deleteIfOwner(long id, String password) {

        CommentEntity comment = commentRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.NO_SUCH_POST));

        if (!comment.getPassword().equals(EncryptUtil.encrypt(password))) {
            throw new ApiException(ErrorCode.NOT_VALID_PASSWORD);
        }

        comment.softDelete();
        commentRepository.save(comment);
    }

    @Transactional
    public void deleteByPostId(long postId) {
        List<CommentEntity> comments = commentRepository.findAllByPostIdAndDeletedFalse(postId);

        for (CommentEntity i : comments) {
            i.softDelete();
        }
    }


}
