package com.romit.ecommerce.controller;

import com.romit.ecommerce.dto.PaymentRequest;
import com.romit.ecommerce.model.Payment;
import com.romit.ecommerce.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/create")
    public Map<String, Object> createPayment(@RequestBody PaymentRequest request) throws Exception {

        Payment payment = paymentService.createPayment(request.getOrderId(), request.getAmount());

        Map<String, Object> response = new HashMap<>();
        response.put("paymentId", payment.getPaymentId());
        response.put("orderId", payment.getOrderId());
        response.put("amount", payment.getAmount());
        response.put("status", payment.getStatus());
        response.put("razorpayOrderId", payment.getPaymentId());

        return response;
    }
}
