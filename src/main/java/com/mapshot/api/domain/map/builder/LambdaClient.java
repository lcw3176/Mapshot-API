package com.mapshot.api.domain.map.builder;


import com.mapshot.api.infra.client.CommonClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LambdaClient {

    private final CommonClient client;

    @Value("${client.lambda.timeout}")
    private Long timeoutMillis;

    @Value("${client.lambda.host}")
    private String host;

    private String path = "";

    public List<MapBuildResponse> sendRequest(MultiValueMap<String, String> queryParams) {

        String url = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host(host)
                .path(path)
                .queryParams(queryParams)
                .build()
                .toString();

        return List.of(client.get(url, timeoutMillis, MapBuildResponse[].class, httpHeaders -> {

        }));
    }
}
