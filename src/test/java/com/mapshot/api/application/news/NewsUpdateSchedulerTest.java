package com.mapshot.api.application.news;

import com.mapshot.api.domain.community.post.PostRepository;
import com.mapshot.api.domain.news.NewsService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class NewsUpdateSchedulerTest {

    @Autowired
    private PostRepository postRepository;

    @MockBean
    private NewsService newsService;

    @Autowired
    private NewsUpdateScheduler newsUpdateScheduler;

    @BeforeEach
    void init() {
        postRepository.deleteAll();
    }

    @AfterEach
    void release() {
        postRepository.deleteAll();
    }

    @Test
    void 발행된_뉴스가_없다면_글을_게시하지_않음() {
        when(newsService.getNewsContent()).
                thenReturn("");

        newsUpdateScheduler.update();

        assertEquals(postRepository.count(), 0);
    }

    @Test
    void 뉴스가_존재하면_글을_게시함() {
        when(newsService.getNewsContent()).
                thenReturn("뉴스뉴스");

        newsUpdateScheduler.update();

        assertEquals(postRepository.count(), 1);
    }
}