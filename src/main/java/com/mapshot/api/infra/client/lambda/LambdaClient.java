package com.mapshot.api.infra.client.lambda;


import com.mapshot.api.infra.client.CommonClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LambdaClient extends CommonClient {


    public <T> List<T> sendRequest(String host, String path, MultiValueMap<String, String> queryParams, Class<T[]> clazz) {
        long timeoutMillis = 30 * 1000L;

        String url = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host(host)
                .path(path)
                .queryParams(queryParams)
                .build()
                .toString();

        return List.of(get(url, timeoutMillis, clazz));
    }
}
