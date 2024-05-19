package com.mapshot.api.domain.news;

import com.mapshot.api.domain.community.post.PostService;
import com.mapshot.api.domain.news.client.gov.TransportGovClient;
import com.mapshot.api.domain.news.client.gov.TransportGovResponse;
import com.mapshot.api.domain.news.client.naver.NaverClient;
import com.mapshot.api.domain.news.client.naver.NaverNewsDto;
import com.mapshot.api.domain.news.client.naver.NaverNewsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final PostService postService;
    private final NaverClient naverClient;
    private final TransportGovClient govClient;

    @Transactional
    @RegisterReflectionForBinding(NaverNewsDto.class)
    public void updateNewsLetter() {
        List<TransportGovResponse> govResponses = govClient.getKeywords();

        if (govResponses.isEmpty()) {
            return;
        }
        
        List<NaverNewsResponse> newsResponses = new ArrayList<>();

        for (TransportGovResponse i : govResponses) {
            NaverNewsResponse news = naverClient.searchNews(i.getKeyword());
            newsResponses.add(news);
        }

        StringBuilder contents = new StringBuilder();
        int index = 1;

        for (NaverNewsResponse i : newsResponses) {
            if (i.getItems().isEmpty()) {
                continue;
            }

            NaverNewsDto detailNews = i.getItems().get(0);

            String lineSpace = wrapHtmlTag("", "p");
            String title = wrapHtmlTag(index++ + ". " + detailNews.getTitle(), "h3");
            String description = wrapHtmlTag(detailNews.getDescription(), "p");
            String link = wrapHtmlTag(detailNews.getOriginallink(), "a");

            contents.append(lineSpace);
            contents.append(title);
            contents.append(lineSpace);
            contents.append(description);
            contents.append(lineSpace);
            contents.append(link);
            contents.append(lineSpace);
        }

        postService.save("뉴스봇", contents.toString(), LocalDate.now().toString() + " 소식 요약", UUID.randomUUID().toString());
    }


    private String wrapHtmlTag(String content, String tag) {
        StringBuilder sb = new StringBuilder();

        if (tag.equals("a")) {
            sb.append("<");
            sb.append(tag);
            sb.append(" ");
            sb.append("href=");
            sb.append(content);
            sb.append(">");
        } else {
            sb.append("<");
            sb.append(tag);
            sb.append(">");
        }

        sb.append(content);

        sb.append("</");
        sb.append(tag);
        sb.append(">");

        return sb.toString();
    }

}
