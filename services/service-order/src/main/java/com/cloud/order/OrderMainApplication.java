package com.cloud.order;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.cloud.order.properties.OrderProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Slf4j
@EnableFeignClients
@EnableDiscoveryClient //开启服务发现功能
@SpringBootApplication
public class OrderMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderMainApplication.class, args);
    }

    //1 项目一启动就监听配置文件变化
    //2 发生变化后拿到变化值
    //3 发送邮件

    @Bean //@Bean标注的函数的参数也将自动注入容器中
    ApplicationRunner applicationRunner(NacosConfigManager  nacosConfigManager) {

        //函数式接口简写
        return args -> {

            ConfigService configService = nacosConfigManager.getConfigService();
            configService.addListener("service-order.properties", "DEFAULT-GROUP", new Listener() {
                @Override
                public Executor getExecutor() {
                    return Executors.newFixedThreadPool(4);//线程池
                }

                @Override
                public void receiveConfigInfo(String configInfo) {
                    log.info("变化的配置信息是：{}", configInfo);
                    log.info("邮件通知。。。");

                }
            });
            System.out.println("=====OrderMainApplication started=====");
        };
    }
}
