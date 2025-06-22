package com.example.shop.service;

import com.example.shop.model.*;
import com.example.shop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RouteServiceImpl implements RouteService {

    private final OrderRepository orderRepository;
    private final ProductService productService;

    @Override
    public RouteResponse calculateRoute(Long orderId) {
        Order order = orderRepository.findById(orderId).get();
        if (order == null || !"SUCCESS".equalsIgnoreCase(order.getStatus())) {
            return new RouteResponse(orderId, "FAIL", new ArrayList<>());
        }

        List<Location> unvisited = new ArrayList<>();
        for (OrderItem item : order.getItems()) {
            Product product = productService.getByName(item.getProductName());
            if (product != null) {
                unvisited.add(product.getLocation());
            }
        }

        List<Location> route = new ArrayList<>();
        Location current = new Location(0, 0);
        route.add(current);

        while (!unvisited.isEmpty()) {
            Location next = findNearest(current, unvisited);
            route.add(next);
            unvisited.remove(next);
            current = next;
        }

        return new RouteResponse(orderId, "SUCCESS", route);
    }

    private Location findNearest(Location from, List<Location> candidates) {
        Location nearest = null;
        double minDist = Double.MAX_VALUE;

        for (Location loc : candidates) {
            double dist = distance(from, loc);
            if (dist < minDist) {
                minDist = dist;
                nearest = loc;
            }
        }

        return nearest;
    }

    private double distance(Location a, Location b) {
        int dx = a.getX() - b.getX();
        int dy = a.getY() - b.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }
}
