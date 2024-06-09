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

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    @Value("${community.post.page_size}")
    private int PAGE_SIZE;

    @Transactional(readOnly = true)
    public Page<PostEntity> getPostsByPageNumber(int pageNumber) {

        if (pageNumber <= 0) {
            pageNumber = 1;
        }

        Pageable pageable = PageRequest.of(--pageNumber, PAGE_SIZE, Sort.by(Sort.Direction.DESC, "id"));

        return postRepository.findAllByDeletedIsFalse(pageable);
    }

    @Transactional
    public void increaseCommentCount(long id) {
        PostEntity postEntity = postRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.NO_SUCH_POST));

        if (postEntity.isDeleted()) {
            throw new ApiException(ErrorCode.NO_SUCH_POST);
        }

        postEntity.increaseCommentCount();
        postRepository.save(postEntity);
    }

    @Transactional
    public void decreaseCommentCount(long id) {
        PostEntity post = postRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.NO_SUCH_POST));

        if (post.isDeleted()) {
            throw new ApiException(ErrorCode.NO_SUCH_POST);
        }

        post.decreaseCommentCount();
        postRepository.save(post);
    }


    @Transactional(readOnly = true)
    public PostEntity getPostById(long id) {
        PostEntity postEntity = postRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.NO_SUCH_POST));

        if (postEntity.isDeleted()) {
            throw new ApiException(ErrorCode.NO_SUCH_POST);
        }

        return postEntity;
    }

    @Transactional
    public long save(String writer, String content, String title, String password) {

        return postRepository.save(PostEntity.builder()
                        .writer(writer)
                        .content(content)
                        .title(title)
                        .password(EncryptUtil.encrypt(password))
                        .commentCount(0)
                        .deleted(false)
                        .build())
                .getId();
    }


    @Transactional
    public void deleteIfOwner(long id, String password) {

        PostEntity post = postRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.NO_SUCH_POST));

        if (!post.getPassword().equals(EncryptUtil.encrypt(password))) {
            throw new ApiException(ErrorCode.NOT_VALID_PASSWORD);
        }

        post.softDelete();
        postRepository.save(post);

    }

    @Transactional
    public void deleteById(long id) {
        PostEntity post = postRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.NO_SUCH_POST));

        post.softDelete();
        postRepository.save(post);
    }


}
