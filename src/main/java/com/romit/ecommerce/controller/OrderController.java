package com.romit.ecommerce.controller;

import com.romit.ecommerce.dto.CreateOrderRequest;
import com.romit.ecommerce.dto.CreateOrderResponse;
import com.romit.ecommerce.dto.OrderHistoryResponse;
import com.romit.ecommerce.model.Order;
import com.romit.ecommerce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public CreateOrderResponse createOrder(@RequestBody CreateOrderRequest request) {
        Order order = orderService.createOrder(request.getUserId());

        CreateOrderResponse response = new CreateOrderResponse();
        response.setId(order.getId());
        response.setUserId(order.getUserId());
        response.setTotalAmount(order.getTotalAmount());
        response.setStatus(order.getStatus());

        return response;
    }


    @GetMapping("/{orderId}")
    public Order getOrder(@PathVariable String orderId) {
        return orderService.getOrderById(orderId);
    }

    @GetMapping("/user/{userId}")
    public List<OrderHistoryResponse> getOrdersByUser(@PathVariable String userId) {
        return orderService.getOrderHistory(userId);
    }

    @PostMapping("/{orderId}/cancel")
    public Order cancelOrder(@PathVariable String orderId) {
        return orderService.cancelOrder(orderId);
    }
}
