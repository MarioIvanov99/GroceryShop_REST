package com.example.shop.service;

import com.example.shop.model.Order;
import com.example.shop.model.OrderItem;
import com.example.shop.model.Product;
import com.example.shop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final AtomicLong idGenerator = new AtomicLong();

    @Override
    public Order placeOrder(List<OrderItem> items) {
        for (OrderItem item : items) {
            Product product = productService.getByName(item.getProductName());
            if (product == null || product.getQuantity() < item.getQuantity()) {
                Order failedOrder = new Order();
                failedOrder.setId(idGenerator.incrementAndGet());
                failedOrder.setItems(items);
                failedOrder.setStatus("FAIL");
                return failedOrder;
            }
        }

        for (OrderItem item : items) {
            Product product = productService.getByName(item.getProductName());
            product.setQuantity(product.getQuantity() - item.getQuantity());
            productService.update(product);
        }

        Order order = new Order();
        order.setItems(items);
        order.setStatus("SUCCESS");
        return orderRepository.save(order);
    }

    @Override
    public Order getOrder(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Override
    public List<Order> listOrders() {
        return orderRepository.findAll();
    }
}
