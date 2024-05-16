package com.mapshot.api.domain.news;

import com.mapshot.api.domain.community.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final PostService postService;
    private final NaverClient naverClient;
    
}
