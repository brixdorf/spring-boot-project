package com.romit.ecommerce.service;

import com.romit.ecommerce.dto.OrderHistoryResponse;
import com.romit.ecommerce.dto.OrderItemResponse;
import com.romit.ecommerce.dto.PaymentSummary;
import com.romit.ecommerce.model.CartItem;
import com.romit.ecommerce.model.Order;
import com.romit.ecommerce.model.OrderItem;
import com.romit.ecommerce.model.Product;
import com.romit.ecommerce.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;

    public Order createOrder(String userId) {

        List<CartItem> cartItems = cartRepository.findByUserId(userId);

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Order order = new Order();
        order.setUserId(userId);
        order.setStatus("CREATED");
        order.setCreatedAt(Instant.now());

        order = orderRepository.save(order);

        double totalAmount = 0.0;
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cartItems) {

            Product product = productRepository.findById(cartItem.getProductId()).orElseThrow(() -> new RuntimeException("Product not found"));

            double itemTotal = product.getPrice() * cartItem.getQuantity();
            totalAmount += itemTotal;

            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(order.getId());
            orderItem.setProductId(product.getId());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(product.getPrice());

            orderItems.add(orderItem);

            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepository.save(product);
        }

        orderItemRepository.saveAll(orderItems);

        order.setTotalAmount(totalAmount);
        orderRepository.save(order);

        cartRepository.deleteByUserId(userId);

        return order;
    }


    public Order getOrderById(String orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

    public List<Order> getOrdersByUser(String userId) {
        return orderRepository.findByUserId(userId);
    }

    public List<OrderHistoryResponse> getOrderHistory(String userId) {

        List<Order> orders = orderRepository.findByUserId(userId);

        return orders.stream().map(order -> {

            OrderHistoryResponse response = new OrderHistoryResponse();
            response.setId(order.getId());
            response.setUserId(order.getUserId());
            response.setTotalAmount(order.getTotalAmount());
            response.setStatus(order.getStatus());
            response.setCreatedAt(order.getCreatedAt());

            List<OrderItem> orderItems = orderItemRepository.findByOrderId(order.getId());

            List<OrderItemResponse> itemResponses = orderItems.stream().map(item -> {
                OrderItemResponse r = new OrderItemResponse();
                r.setProductId(item.getProductId());
                r.setQuantity(item.getQuantity());
                r.setPrice(item.getPrice());
                return r;
            }).toList();

            response.setItems(itemResponses);


            paymentRepository.findByOrderId(order.getId()).ifPresent(payment -> {
                PaymentSummary summary = new PaymentSummary();
                summary.setId(payment.getPaymentId());
                summary.setStatus(payment.getStatus());
                summary.setAmount(payment.getAmount());
                response.setPayment(summary);
            });

            return response;

        }).toList();
    }


    public Order cancelOrder(String orderId) {

        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));

        if ("PAID".equals(order.getStatus())) {
            throw new RuntimeException("Paid orders cannot be cancelled");
        }

        order.setStatus("CANCELLED");
        return orderRepository.save(order);
    }
}
