package com.mapshot.api.domain.community.post;


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
public class PostService {

    private final PostRepository postRepository;

    @Value("${community.post.page_size}")
    private int PAGE_SIZE;

    @Transactional(readOnly = true)
    public PostListResponse getPostListByPageNumber(int pageNumber) {

        if (pageNumber <= 0) {
            pageNumber = 1;
        }

        Pageable pageable = PageRequest.of(--pageNumber, PAGE_SIZE, Sort.by(Sort.Direction.DESC, "id"));
        Page<PostEntity> pages = postRepository.findAll(pageable);

        List<PostEntity> postEntities = pages.getContent();

        List<PostDto> postDtos = postEntities.stream()
                .map(i -> PostDto.builder()
                        .id(i.getId())
                        .title(i.getTitle())
                        .createdDate(i.getCreatedDate())
                        .writer(i.getWriter())
                        .commentCount(i.getCommentCount())
                        .build())
                .toList();

        return PostListResponse.builder()
                .posts(postDtos)
                .totalPage(pages.getTotalPages())
                .build();
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
