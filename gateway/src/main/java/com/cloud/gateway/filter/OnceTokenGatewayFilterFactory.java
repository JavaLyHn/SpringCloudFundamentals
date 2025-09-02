package com.cloud.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractNameValueGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class OnceTokenGatewayFilterFactory extends AbstractNameValueGatewayFilterFactory {
    @Override
    public GatewayFilter apply(NameValueConfig config) {
        return new GatewayFilter() {
            @Override
            public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
                //每次响应之前 添加一个一次性令牌 支持uuid jwt等各种格式
                return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                    ServerHttpResponse response = exchange.getResponse();
                    HttpHeaders headers = response.getHeaders();
                    String value = config.getValue();
                    if("uuid".equalsIgnoreCase(value)){
                        value= UUID.randomUUID().toString();
                    }else if("jwt".equalsIgnoreCase(value)){
                        value="eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOiJhMWIyYzM0ZC01NjY3LTg5ODAtYWJjZGUtZjBnMTRoNGk1ajZrIiwidXNlcm5hbWUiOiJ1c2VyXzEyMzQ1Njc4IiwiZW1haWwiOiJ1c2VyXzEyMzQ1Njc4QGV4YW1wbGUuY29tIiwicm9sZSI6IkFETUlOIiwidGltZXN0YW1wIjoxNjk4NzY1NDMyfQ.signature";
                    }
                    headers.add(config.getName(),value);
                }));
            }
        };
    }
}
