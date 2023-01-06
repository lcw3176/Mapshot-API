package com.joebrooks.mapshot.filter;

import com.joebrooks.mapshot.filter.NoticeCustomFilter.Config;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class NoticeCustomFilter extends AbstractGatewayFilterFactory<Config> {

    public NoticeCustomFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {

        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            if (!Objects.equals(request.getMethod(), HttpMethod.GET)) {
                return handleBadRequest(exchange);
            }

            return chain.filter(exchange);
        };
    }

    private Mono<Void> handleBadRequest(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();

        response.setStatusCode(HttpStatus.BAD_REQUEST);
        return response.setComplete();
    }

    public static class Config {
   
    }
}
