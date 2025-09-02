package com.cloud.product.service.impl;

import com.cloud.product.bean.Product;
import com.cloud.product.service.ProductService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ProductServiceImpl implements ProductService {


    @Override
    public Product getProductById(Long productId) {
        Product product = new Product();
        product.setId(productId);
        product.setPrice(new BigDecimal("99"));
        product.setProductName("苹果-" + productId);
        product.setNum(3);
        return product;
    }
}
