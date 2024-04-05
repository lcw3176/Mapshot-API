package com.mapshot.api.domain.community.post;


import com.mapshot.api.infra.encrypt.EncryptUtil;
import com.mapshot.api.infra.exception.ApiException;
import com.mapshot.api.infra.exception.status.ErrorCode;
import com.mapshot.api.presentation.community.post.model.PostDetailResponse;
import com.mapshot.api.presentation.community.post.model.PostListResponse;
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
    public long save(String writer, String content, String title, String password) {

        return postRepository.save(PostEntity.builder()
                        .writer(writer)
                        .content(content)
                        .title(title)
                        .password(EncryptUtil.encrypt(password))
                        .commentCount(0)
                        .build())
                .getId();
    }


    @Transactional
    public void deleteIfOwner(long id, String password) {

        PostEntity entity = postRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.NO_SUCH_POST));

        if (!entity.getPassword().equals(EncryptUtil.encrypt(password))) {
            throw new ApiException(ErrorCode.NOT_VALID_PASSWORD);
        }

        postRepository.deleteById(id);
    }

}
