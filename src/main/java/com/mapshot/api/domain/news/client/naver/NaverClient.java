package com.mapshot.api.domain.news.client.naver;

import com.mapshot.api.infra.client.CommonClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class NaverClient {


    private final CommonClient client;

    @Value("${client.naver.host}")
    private String host;

    @Value("${client.naver.path}")
    private String path;

    @Value("${client.naver.id}")
    private String headerId;

    @Value("${client.naver.secret}")
    private String headerSecret;

    public NaverNewsResponse searchNews(String query) {
        long timeoutMillis = 3000;

        String url = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host(host)
                .path(path)
                .queryParam("query", query)
                .build()
                .toString();

        return client.get(url, timeoutMillis, NaverNewsResponse.class, httpHeaders -> {
            httpHeaders.add("X-Naver-Client-Id", headerId);
            httpHeaders.add("X-Naver-Client-Secret", headerSecret);
        });
    }

}