package com.cloud.order.controller;

import com.cloud.order.bean.Order;
import com.cloud.order.properties.OrderProperties;
import com.cloud.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope //配置属性的自动刷新
@RestController
public class OrderController {

    @Autowired
    OrderService orderService;
//    @Value("${order.timeout}")
//    String orderTimeOut;
//    @Value("${order.auto-confirm}")
//    String orderAutoConfirm;

    @Autowired
    OrderProperties  orderProperties;

    @GetMapping("/create")
    public Order createOrder(@RequestParam("userId") Long userId,
                             @RequestParam("productId") Long productId){

        Order order = orderService.createOrder(productId, userId);
        return  order;
    }

    @GetMapping("/value")
    public String getValue(){
        return "orderTimeOut:" + orderProperties.getTimeout() + "orderAutoConfirm:" + orderProperties.getAutoConfirm() +"orderDbUrl" +  orderProperties.getDbUrl();
    }
}
