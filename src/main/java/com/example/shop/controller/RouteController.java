package com.example.shop.controller;

import com.example.shop.model.RouteResponse;
import com.example.shop.service.RouteService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/routes")
public class RouteController {

    private final RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @GetMapping("/{orderId}")
    public RouteResponse getRoute(@PathVariable Long orderId) {
        return routeService.calculateRoute(orderId);
    }
}
