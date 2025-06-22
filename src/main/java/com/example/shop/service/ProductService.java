package com.example.shop.service;

import com.example.shop.model.Product;
import com.example.shop.repository.ProductRepository;

import java.util.List;

public interface ProductService {
    Product create(Product product);
    List<Product> list();
    Product getByName(String name);
    void update(Product product);
    void delete(Long id);
}
