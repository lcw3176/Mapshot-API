package com.mapshot.api.domain.community.comment;


import com.mapshot.api.domain.community.post.PostEntity;
import com.mapshot.api.domain.community.post.PostRepository;
import com.mapshot.api.infra.encrypt.EncryptUtil;
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
    private final PostRepository postRepository;

    @Value("${community.comment.page_size}")
    private int PAGE_SIZE;

    @Transactional(readOnly = true)
    public CommentResponse getComments(int pageNumber, long postId) {

        if (pageNumber <= 0) {
            pageNumber = 1;
        }

        Pageable pageable = PageRequest.of(--pageNumber, PAGE_SIZE, Sort.by(Sort.Direction.ASC, "id"));
        Page<CommentEntity> pages = commentRepository.findAllByPostIdAndDeletedFalse(pageable, postId);

        List<CommentEntity> commentEntities = pages.getContent();

        List<CommentDto> commentDtos = commentEntities.stream()
                .map(i -> CommentDto.builder()
                        .id(i.getId())
                        .createdDate(i.getCreatedDate())
                        .writer(i.getWriter())
                        .content(i.getContent())
                        .build())
                .toList();

        return CommentResponse.builder()
                .comments(commentDtos)
                .totalPage(pages.getTotalPages())
                .build();
    }


    @Transactional
    public long save(String writer, String content, long postId, String password) {


        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new ApiException(ErrorCode.NO_SUCH_POST));

        long commentId = commentRepository.save(CommentEntity.builder()
                        .writer(writer)
                        .content(content)
                        .password(EncryptUtil.encrypt(password))
                        .deleted(false)
                        .postId(postId)
                        .build())
                .getId();

        post.increaseCommentCount();
        postRepository.save(post);

        return commentId;
    }


    @Transactional
    public void deleteIfOwner(long id, String password) {

        CommentEntity entity = commentRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.NO_SUCH_POST));

        if (!entity.getPassword().equals(EncryptUtil.encrypt(password))) {
            throw new ApiException(ErrorCode.NOT_VALID_PASSWORD);
        }

        PostEntity post = postRepository.findById(entity.getPostId())
                .orElseThrow(() -> new ApiException(ErrorCode.NO_SUCH_POST));

        commentRepository.deleteById(id);

        post.decreaseCommentCount();
        postRepository.save(post);
    }

}
