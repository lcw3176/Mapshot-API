package com.mapshot.api.domain.news.client.naver;

import com.mapshot.api.infra.client.ApiHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class NaverClient {


    private final RestClient restClient;
    @Value("${client.naver.id}")
    private String headerId;
    @Value("${client.naver.secret}")
    private String headerSecret;
    @Value("${client.naver.url}")
    private String url;

    public NaverNewsResponse searchNews(String query) {

        return ApiHandler.handle(() -> restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(url)
                        .queryParam("query", query)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .headers(httpHeaders -> {
                    httpHeaders.add("X-Naver-Client-Id", headerId);
                    httpHeaders.add("X-Naver-Client-Secret", headerSecret);
                })
                .retrieve()
                .onStatus(HttpStatusCode::isError, ((request, response) -> {
                    throw new RuntimeException("status: " + response.getStatusCode() + " body: " + response.getBody());
                }))
                .body(NaverNewsResponse.class));

    }

}
