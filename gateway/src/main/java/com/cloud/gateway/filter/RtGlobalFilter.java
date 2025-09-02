package com.cloud.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class RtGlobalFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String uri = request.getURI().toString();
        long start = System.currentTimeMillis();
        log.info("请求【{}】开始时间：{}",uri,start);
        //============以上是前置逻辑================

        //响应式编程是全异步进行的 所以不等放行完 后置逻辑会立即触发
//        Mono<Void> filter = chain.filter(exchange);

        //===========以下是后置逻辑=================
        //下面是真正的后置逻辑
        Mono<Void> filter = chain.filter(exchange)
                .doFinally((result) ->{
                    long end = System.currentTimeMillis();
                    log.info("请求【{}】结束时间：{}",uri,end);
                });

        return filter;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
