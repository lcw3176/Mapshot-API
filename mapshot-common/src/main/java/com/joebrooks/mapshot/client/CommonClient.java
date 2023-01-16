package com.joebrooks.mapshot.client;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

public abstract class CommonClient {

    private WebClient getClient(String baseUrl) {
        int timeoutMillis = 3000;

        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, timeoutMillis)
                .responseTimeout(Duration.ofMillis(timeoutMillis))
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(timeoutMillis, TimeUnit.MILLISECONDS))  //sec
                                .addHandlerLast(new WriteTimeoutHandler(timeoutMillis, TimeUnit.MILLISECONDS)) //sec
                );

        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeaders(headers -> headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    protected <T> T post(String path, String body, Class<T> clazz) {

        try {
            return getClient(path).post()
                    .accept(MediaType.APPLICATION_JSON)
                    .acceptCharset(StandardCharsets.UTF_8)
                    .bodyValue(body)
                    .retrieve()
                    .onStatus(HttpStatus::isError, response -> response.bodyToMono(String.class)
                            .flatMap(errorBody -> Mono.error(new RuntimeException(errorBody))))
                    .bodyToMono(clazz)
                    .block();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    protected <T> T get(String path, Class<T> clazz) {

        try {
            return getClient(path).get()
                    .uri(path)
                    .accept(MediaType.APPLICATION_JSON)
                    .acceptCharset(StandardCharsets.UTF_8)
                    .retrieve()
                    .onStatus(HttpStatus::isError, response -> response.bodyToMono(String.class)
                            .flatMap(errorBody -> Mono.error(new RuntimeException(errorBody))))
                    .bodyToMono(clazz)
                    .block();

        } catch (Exception e) {
            throw new RuntimeException(path, e);
        }
    }
}
