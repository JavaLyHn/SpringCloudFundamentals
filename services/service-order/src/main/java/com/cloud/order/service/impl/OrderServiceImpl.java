package com.cloud.order.service.impl;

import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.cloud.order.bean.Order;
import com.cloud.order.feign.ProductFeignClient;
import com.cloud.order.service.OrderService;
import com.cloud.product.bean.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    DiscoveryClient discoveryClient;

    @Autowired
    LoadBalancerClient loadBalancerClient;

    @Autowired
    ProductFeignClient productFeignClient;

    @Autowired
    RestTemplate restTemplate;

    @SentinelResource(value = "createOrder",blockHandler = "createOrderFallback")
    @Override
    public Order createOrder(Long productId, Long userId) {
//        Product product = getProductFromRemote(productId);
        Product product = productFeignClient.getProductById(productId);
        Order order = new Order();
        order.setId(productId);
        order.setUserId(userId);
        //总金额
        order.setTotalAmount(product.getPrice().multiply(new BigDecimal(product.getNum())));
        order.setNickName("lyhn");
        order.setAddress("江苏");
        //远程查询商品列表
        order.setProductList(Arrays.asList(product));
        return order;
    }

    public Order createOrderFallback(Long productId, Long userId, BlockException e) {
        Order order = new Order();
        order.setId(productId);
        order.setUserId(userId);
        order.setTotalAmount(new BigDecimal(0));
        order.setNickName("未知用户");
        order.setAddress("未知地区" + e.getMessage());

//        try {
//            SphU.entry("haha");
//        } catch (BlockException ex) {
//            throw new RuntimeException(ex);
//        }

        return order;

    }

    private Product getProductFromRemote(Long productId) {
        //1 获取远程商品服务机器
        List<ServiceInstance> instances = discoveryClient.getInstances("service-product");
        ServiceInstance serviceInstance = instances.get(0);
        //远程url
        String url = "http://" + serviceInstance.getHost() + ":" + serviceInstance.getPort() + "/product/" +  productId;
        log.info("远程请求:{}",url);
        //2 给远程发送请求
        Product product = restTemplate.getForObject(url, Product.class);
        return product;

    }

    //完成负载均衡发送请求
    private Product getProductFromRemoteByLoadBalancer(Long productId) {
        //1 获取远程商品服务机器(负载均衡)
        ServiceInstance choose = loadBalancerClient.choose("service-product");

        //远程url
        String url = "http://" + choose.getHost() + ":" + choose.getPort() + "/product/" +  productId;
        log.info("远程请求:{}",url);
        //2 给远程发送请求
        Product product = restTemplate.getForObject(url, Product.class);
        return product;

    }

    //完成基于注解的负载均衡发送请求
    private Product getProductFromRemoteByLoadBalancerAnnotation(Long productId) {
        //注解在configuration中
        String url = "http://service-product/product/" +  productId;
        //给远程发送请求
        Product product = restTemplate.getForObject(url, Product.class);
        return product;

    }
}
