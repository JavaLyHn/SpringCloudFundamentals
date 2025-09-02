package com.cloud.order.config;

import feign.Logger;
import feign.Retryer;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

@Configuration
public class OrderConfig {

    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    //详细打印OpenFeign远程调用过程
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    //重试器
    @Bean
    Retryer feignRetryer() {
        return new Retryer.Default();
//        this(100L, TimeUnit.SECONDS.toMillis(1L), 5);

    }
}
