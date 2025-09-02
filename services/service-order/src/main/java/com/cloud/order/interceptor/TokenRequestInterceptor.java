package com.cloud.order.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TokenRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        System.out.println("TokenRequestInterceptor=====");
        requestTemplate.header("X-Token", UUID.randomUUID().toString());
    }
}
