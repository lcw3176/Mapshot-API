package com.mapshot.api.domain.news;

import com.mapshot.api.infra.client.CommonClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
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

    public NewsResponse sendRequest(MultiValueMap<String, String> queryParams) {
        long timeoutMillis = 3000;

        String url = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host(host)
                .path(path)
                .queryParams(queryParams)
                .build()
                .toString();

        return client.get(url, timeoutMillis, NewsResponse.class, httpHeaders -> {
            httpHeaders.add("X-Naver-Client-Id", headerId);
            httpHeaders.add("X-Naver-Client-Secret", headerSecret);
        });
    }

}
