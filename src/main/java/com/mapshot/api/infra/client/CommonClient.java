package com.mapshot.api.infra.client;

import com.mapshot.api.infra.exception.ApiException;
import com.mapshot.api.infra.exception.status.ErrorCode;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Component
public class CommonClient {

    private WebClient getClient(String baseUrl, long timeoutMillis) {
        int baseTimeoutMillis = (int) timeoutMillis;

        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, baseTimeoutMillis)
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(baseTimeoutMillis, TimeUnit.MILLISECONDS))  //sec
                                .addHandlerLast(new WriteTimeoutHandler(baseTimeoutMillis, TimeUnit.MILLISECONDS)) //sec
                );

        return WebClient.builder()
                .baseUrl(baseUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    public <T> T post(String path, long timeoutMillis, String body, Class<T> clazz) {

        try {
            return getClient(path, timeoutMillis).post()
                    .accept(MediaType.APPLICATION_JSON)
                    .acceptCharset(StandardCharsets.UTF_8)
                    .bodyValue(body)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, response -> response.bodyToMono(String.class)
                            .flatMap(errorBody -> Mono.error(new RuntimeException(errorBody))))
                    .bodyToMono(clazz)
                    .block(Duration.ofMillis(timeoutMillis));

        } catch (Exception e) {
            throw new ApiException(ErrorCode.SERVER_TO_SERVER_ERROR);
        }
    }


    public <T> T[] get(String path, long timeoutMillis, Class<T[]> clazz) {

        try {
            return getClient(path, timeoutMillis).get()
                    .accept(MediaType.APPLICATION_JSON)
                    .acceptCharset(StandardCharsets.UTF_8)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, response -> response.bodyToMono(String.class)
                            .flatMap(errorBody -> Mono.error(new RuntimeException(errorBody))))
                    .bodyToMono(clazz)
                    .block(Duration.ofMillis(timeoutMillis));

        } catch (Exception e) {
            throw new ApiException(ErrorCode.SERVER_TO_SERVER_ERROR);
        }
    }
}
