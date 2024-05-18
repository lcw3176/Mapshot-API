package com.mapshot.api.infra.client.naver;

import com.mapshot.api.infra.client.ExternalApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class NaverWebClientConfig {


    @Value("${client.naver.host}")
    private String host;

    private static final long TIMEOUT_MILLIS = 3000;

    @Bean
    public WebClient naverRestClient() {
        return ExternalApiClient.getClient(host, TIMEOUT_MILLIS);
    }
}
