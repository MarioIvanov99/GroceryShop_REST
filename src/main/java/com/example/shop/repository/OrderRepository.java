package com.example.shop.repository;

import com.example.shop.model.Order;
import com.example.shop.model.Product;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class OrderRepository {
    private final Map<Long, Order> store = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong();

    public Order save(Order order) {
        Long id = idGenerator.incrementAndGet();
        order.setId(id);
        store.put(id, order);
        return order;
    }

    public Optional<Order> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    public List<Order> findAll() {
        return new ArrayList<>(store.values());
    }
}
