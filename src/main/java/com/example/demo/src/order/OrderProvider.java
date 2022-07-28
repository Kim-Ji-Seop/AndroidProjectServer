package com.example.demo.src.order;

import com.example.demo.src.product.ProductDao;
import org.springframework.stereotype.Service;

@Service
public class OrderProvider {
    private final OrderDao orderDao;
    public OrderProvider(OrderDao orderDao){
        this.orderDao=orderDao;
    }
}
