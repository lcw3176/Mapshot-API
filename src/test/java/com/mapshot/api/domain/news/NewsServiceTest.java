package com.mapshot.api.domain.news;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class NewsServiceTest {

    @Autowired
    private NewsService newsService;


    @Test
    void 검색_정확도를_위해_대괄호_안의_문자열은_제거한다() {
        String target = "[차관동정] 국토교통부, 고흥·울진·안동 신규 국가산단 예타 본격 추진";
        String result = newsService.removeBigBracket(target);
        String expected = " 국토교통부, 고흥·울진·안동 신규 국가산단 예타 본격 추진";

        assertEquals(result, expected);
    }
}