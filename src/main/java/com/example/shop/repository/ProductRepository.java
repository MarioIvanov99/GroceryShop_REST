package com.example.shop.repository;

import com.example.shop.model.Product;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ProductRepository {
    private final Map<Long, Product> store = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong();

    public Product save(Product product) {
        Long id = idGenerator.incrementAndGet();
        product.setId(id);
        store.put(id, product);
        return product;
    }

    public List<Product> findAll() {
        return new ArrayList<>(store.values());
    }

    public Optional<Product> findByName(String name) {
        return store.values().stream()
                .filter(p -> p.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    public void update(Product product) {
        store.put(product.getId(), product);
    }

    public void delete(Long id) {
        store.remove(id);
    }
}
