package com.mapshot.api.application.news;

import com.mapshot.api.domain.community.post.PostService;
import com.mapshot.api.domain.news.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class NewsUpdateScheduler {

    private final NewsService newsService;
    private final PostService postService;

    // 매일 오후 9시에 당일 국토교통부 보도자료 업데이트
    @Scheduled(cron = "0 0 21 * * *")
    public void update() {
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
