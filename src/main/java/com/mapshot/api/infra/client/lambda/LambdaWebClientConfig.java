package com.mapshot.api.infra.client.lambda;

import com.mapshot.api.infra.client.ExternalApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class LambdaWebClientConfig {


    @Value("${client.lambda.timeout}")
    private Long timeoutMillis;

    @Value("${client.lambda.url}")
    private String url;


    @Bean
    public WebClient lambdaRestClient() {
        return ExternalApiClient.getClient(url, timeoutMillis);
    }
}
