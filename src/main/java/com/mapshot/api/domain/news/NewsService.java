package com.mapshot.api.domain.news;

import com.mapshot.api.domain.news.client.gov.TransportGovClient;
import com.mapshot.api.domain.news.client.gov.TransportGovResponse;
import com.mapshot.api.domain.news.client.naver.NaverClient;
import com.mapshot.api.domain.news.client.naver.NaverNewsDto;
import com.mapshot.api.domain.news.client.naver.NaverNewsResponse;
import com.mapshot.api.infra.util.HtmlTagUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final NaverClient naverClient;
    private final TransportGovClient govClient;


    public String getNewsContent() {
        List<TransportGovResponse> govResponses = govClient.getKeywords();

        if (govResponses.isEmpty()) {
            return "";
        }

        List<NaverNewsResponse> newsResponses = new ArrayList<>();

        for (TransportGovResponse i : govResponses) {
            String filteredKeyword = removeBigBracket(i.getKeyword());
            NaverNewsResponse news = naverClient.searchNews(filteredKeyword);
            newsResponses.add(news);
        }


        int index = 1;
        StringBuilder contents = new StringBuilder();

        for (NaverNewsResponse i : newsResponses) {
            if (i.getItems().isEmpty() || i.getTotal() == 0) {
                continue;
            }

            NaverNewsDto detailNews = i.getItems().get(0);

            if (detailNews.getPubDate().isBefore(LocalDateTime.now().minusDays(1))) {
                continue;
            }

            contents.append(makeNewsContentForm(index++, detailNews));
        }

        return contents.toString();
    }


    public String makeNewsContentForm(int index, NaverNewsDto news) {
        StringBuilder contents = new StringBuilder();

        String lineSpace = HtmlTagUtil.wrapHtmlTag("", "p");
        String title = HtmlTagUtil.wrapHtmlTag(index + ". " + news.getTitle(), "h3");
        String description = HtmlTagUtil.wrapHtmlTag(news.getDescription(), "p");
        String link = HtmlTagUtil.wrapHtmlTag(news.getOriginallink(), "a", Map.of("href", news.getOriginallink()));

        String imageLink = HtmlTagUtil.getMetaTagImage(news.getOriginallink());
        imageLink = HtmlTagUtil.wrapHtmlTag("", "img", Map.of("src", imageLink));


        contents.append(lineSpace);
        contents.append(title);
        contents.append(lineSpace);
        contents.append(imageLink);
        contents.append(lineSpace);
        contents.append(description);
        contents.append(lineSpace);
        contents.append(link);
        contents.append(lineSpace);

        return contents.toString();
    }

    public String removeBigBracket(String str) {
        if (str.contains("[")) {
            StringBuilder sb = new StringBuilder(str);
            int startIndex = sb.indexOf("[");
            int endIndex = sb.indexOf("]");

            sb.delete(startIndex, endIndex + 1);

            return sb.toString();
        }

        return str;
    }


}
