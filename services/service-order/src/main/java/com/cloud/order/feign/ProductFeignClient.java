package com.cloud.order.feign;

import com.cloud.order.feign.fallback.ProductFeignClientFallback;
import com.cloud.product.bean.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "service-product",fallback = ProductFeignClientFallback.class) //feign客户端
public interface ProductFeignClient {

    //mvc注解的两套使用逻辑
    //1 标注在controller上是接收这样的请求
    //2 标注在FeignClient上是发送这样的请求
    @GetMapping("/product/{id}")
    Product getProductById(@PathVariable("id") Long id);
    //void getProductById(@PathVariable("id") Long id,@RequestHeader("token") String token);
    //如果像上面这样写 则意思是在请求头上附加token值
}
