package com.mapshot.api.domain.map.builder;


import com.mapshot.api.infra.client.ApiHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
@RequiredArgsConstructor
public class LambdaClient {

    private final WebClient lambdaRestClient;


    public List<MapBuildResponse> sendRequest(MultiValueMap<String, String> queryParams) {

        return ApiHandler.handle(() -> lambdaRestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParams(queryParams)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> response.bodyToMono(String.class)
                        .flatMap(errorBody -> Mono.error(new RuntimeException(errorBody))))
                .bodyToFlux(MapBuildResponse.class)
                .collectList()
                .block());
    }
}
