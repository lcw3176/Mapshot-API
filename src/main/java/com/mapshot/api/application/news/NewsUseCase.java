package com.mapshot.api.application.news;

import com.mapshot.api.domain.community.post.PostService;
import com.mapshot.api.domain.news.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class NewsUseCase {

    private final NewsService newsService;
    private final PostService postService;

    public void updateNewsLetter() {
        String content = newsService.getNewsContent();
        String writer = "헤드샷";
        String title = "[" + LocalDate.now() + "] 오늘의 헤드라인";
        String password = UUID.randomUUID().toString();

        postService.save(writer, content, title, password);
    }

}
