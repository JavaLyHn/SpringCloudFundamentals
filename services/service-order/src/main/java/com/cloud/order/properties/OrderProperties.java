package com.cloud.order.properties;


import com.cloud.order.service.OrderService;
import lombok.Data;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "order")//配置批量绑定在nacos下 无需@RefershScope自动刷新
@Data
public class OrderProperties {

    String timeout;
    String autoConfirm;
    String dbUrl;
}
