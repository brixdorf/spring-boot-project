package com.romit.ecommerce.dto;

import java.time.Instant;
import java.util.List;

public class OrderHistoryResponse {

    private String id;
    private String userId;
    private double totalAmount;
    private String status;
    private Instant createdAt;
    private List<OrderItemResponse> items;
    private PaymentSummary payment;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public List<OrderItemResponse> getItems() {
        return items;
    }

    public void setItems(List<OrderItemResponse> items) {
        this.items = items;
    }

    public PaymentSummary getPayment() {
        return payment;
    }

    public void setPayment(PaymentSummary payment) {
        this.payment = payment;
    }
}
