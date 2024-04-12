package com.mapshot.api.infra.client.lambda;


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

    @Value("${lambda.timeout}")
    private Long timeoutMillis;

    public <T> List<T> sendRequest(String host, String path, MultiValueMap<String, String> queryParams, Class<T[]> clazz) {

        String url = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host(host)
                .path(path)
                .queryParams(queryParams)
                .build()
                .toString();

        return List.of(client.get(url, timeoutMillis, clazz));
    }
}
