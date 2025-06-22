package com.example.shop.service;

import com.example.shop.model.RouteResponse;

public interface RouteService {
    RouteResponse calculateRoute(Long orderId);
}
