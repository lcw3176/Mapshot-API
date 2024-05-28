package com.mapshot.api.domain.news;

import com.mapshot.api.domain.community.post.PostService;
import com.mapshot.api.domain.news.client.gov.TransportGovClient;
import com.mapshot.api.domain.news.client.gov.TransportGovResponse;
import com.mapshot.api.domain.news.client.naver.NaverClient;
import com.mapshot.api.domain.news.client.naver.NaverNewsDto;
import com.mapshot.api.domain.news.client.naver.NaverNewsResponse;
import com.mapshot.api.infra.util.HtmlWrapper;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
            String filteredKeyword = removeBigBracket(i.getKeyword());
            NaverNewsResponse news = naverClient.searchNews(filteredKeyword);
            newsResponses.add(news);
        }


        int index = 1;
        StringBuilder contents = new StringBuilder();

        for (NaverNewsResponse i : newsResponses) {
            if (i.getItems().isEmpty()) {
                continue;
            }

            NaverNewsDto detailNews = i.getItems().get(0);
            contents.append(makeNewsContentForm(index++, detailNews));
        }

        postService.save("헤드샷", contents.toString(), "[" + LocalDate.now() + "] 오늘의 헤드라인", UUID.randomUUID().toString());
    }


    public String makeNewsContentForm(int index, NaverNewsDto news) {
        StringBuilder contents = new StringBuilder();

        String lineSpace = HtmlWrapper.wrapHtmlTag("", "p");
        String title = HtmlWrapper.wrapHtmlTag(index + ". " + news.getTitle(), "h3");
        String description = HtmlWrapper.wrapHtmlTag(news.getDescription(), "p");
        String link = HtmlWrapper.wrapHtmlTag(news.getOriginallink(), "a", Map.of("href", news.getOriginallink()));

        String imageLink = getMetaImage(news.getOriginallink());
        imageLink = HtmlWrapper.wrapHtmlTag("", "img", Map.of("src", imageLink));


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


    public String getMetaImage(String url) {
        Document document = null;

        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Elements meta = document.getElementsByTag("meta");

        for (Element i : meta) {
            String prop = i.attr("property");

            if (prop.equals("og:image")) {
                return i.attr("content");
            }
        }

        return "";
    }

}
