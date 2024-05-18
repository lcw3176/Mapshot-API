package com.mapshot.api.domain.news.client.naver;

import com.mapshot.api.infra.client.ApiHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class NaverClient {


    private final WebClient naverRestClient;

    @Value("${client.naver.path}")
    private String path;

    @Value("${client.naver.id}")
    private String headerId;

    @Value("${client.naver.secret}")
    private String headerSecret;

    public NaverNewsResponse searchNews(String query) {

        return ApiHandler.handle(() -> naverRestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(path)
                        .queryParam("query", query)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .headers(httpHeaders -> {
                    httpHeaders.add("X-Naver-Client-Id", headerId);
                    httpHeaders.add("X-Naver-Client-Secret", headerSecret);
                })
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> response.bodyToMono(String.class)
                        .flatMap(errorBody -> Mono.error(new RuntimeException(errorBody))))
                .bodyToMono(NaverNewsResponse.class)
                .block());

    }

}
