package com.mapshot.api.application.news;

import com.mapshot.api.domain.news.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NewsUpdateScheduler {

    private final NewsService newsService;

    // 매일 오후 9시에 당일 국토교통부 보도자료 업데이트
    @Scheduled(cron = "0 0 21 * * *")
    public void update() {
        newsService.updateNews();
    }

}
