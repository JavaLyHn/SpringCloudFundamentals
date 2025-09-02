package com.cloud.order.service;

import com.cloud.order.bean.Order;

public interface OrderService {
    Order createOrder(Long productId,Long userId);
}
