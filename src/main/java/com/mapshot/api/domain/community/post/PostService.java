package com.mapshot.api.domain.community.post;


import com.mapshot.api.infra.encrypt.EncryptUtil;
import com.mapshot.api.infra.exception.ApiException;
import com.mapshot.api.infra.exception.status.ErrorCode;
import com.mapshot.api.presentation.community.post.model.PostDetailResponse;
import com.mapshot.api.presentation.community.post.model.PostListResponse;
import com.mapshot.api.presentation.community.post.model.PostRegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    public List<PostListResponse> getPostListById(long id) {

        if (id <= 0) {
            id = postRepository.findFirstByOrderByIdDesc().getId() + 1;
        }

        List<PostEntity> postEntities = postRepository.findTop10ByIdLessThanOrderByIdDesc(id);

        return postEntities.stream()
                .map(i -> PostListResponse.builder()
                        .id(i.getId())
                        .title(i.getTitle())
                        .createdDate(i.getCreatedDate())
                        .writer(i.getWriter())
                        .commentCount(i.getCommentCount())
                        .build())
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public PostDetailResponse getSinglePostById(long id) {
        PostEntity postEntity = postRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.NO_SUCH_POST));

        return PostDetailResponse.builder()
                .id(postEntity.getId())
                .title(postEntity.getTitle())
                .writer(postEntity.getWriter())
                .content(postEntity.getContent())
                .createdDate(postEntity.getCreatedDate())
                .build();
    }

    @Transactional
    public long save(PostRegisterRequest request) {

        return postRepository.save(PostEntity.builder()
                        .writer(request.getWriter())
                        .content(request.getContent())
                        .title(request.getTitle())
                        .password(EncryptUtil.encrypt(request.getPassword()))
                        .commentCount(0)
                        .build())
                .getId();
    }

}
