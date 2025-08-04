package com.mapshot.api.domain.news;

import com.mapshot.api.domain.community.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NewsUseCase {

    private final NewsService newsService;
    private final PostService postService;

    public void updateNews() {
        String content = newsService.getNewsContent();

        if (content.isBlank()) {
            return;
        }

        String writer = "헤드샷";
        String title = "[" + LocalDate.now() + "] 오늘의 헤드라인";
        String password = UUID.randomUUID().toString();

        postService.save(writer, content, title, password);
    }


}
