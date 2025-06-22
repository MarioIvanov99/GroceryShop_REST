package com.example.shop.service;

import com.example.shop.model.Order;
import com.example.shop.model.OrderItem;
import com.example.shop.model.Product;

import java.util.List;

public interface OrderService {
    Order placeOrder(List<OrderItem> items);
    Order getOrder(Long id);
    List<Order> listOrders();
}
