package com.mapshot.api.domain.admin.community.post;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminPostService {

    private final AdminPostRepository adminPostRepository;

    @Transactional
    public void deletePost(long id) {
        adminPostRepository.deleteById(id);
    }


}
