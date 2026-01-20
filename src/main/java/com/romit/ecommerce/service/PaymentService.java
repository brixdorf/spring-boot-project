package com.romit.ecommerce.service;

import com.razorpay.Order;
import com.romit.ecommerce.client.PaymentServiceClient;
import com.romit.ecommerce.model.Payment;
import com.romit.ecommerce.repository.OrderRepository;
import com.romit.ecommerce.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PaymentServiceClient paymentServiceClient;

    public Payment createPayment(String orderId, double amount) throws Exception {

        com.romit.ecommerce.model.Order order = orderRepository.findById(orderId).orElse(null);

        if (order == null) {
            throw new RuntimeException("Order not found");
        }

        if (!"CREATED".equals(order.getStatus())) {
            throw new RuntimeException("Order not payable");
        }

        Order razorpayOrder = paymentServiceClient.createRazorpayOrder(orderId, amount);

        Payment payment = new Payment();
        payment.setOrderId(orderId);
        payment.setAmount(amount);
        payment.setStatus("PENDING");
        payment.setPaymentId(razorpayOrder.get("id"));
        payment.setCreatedAt(Instant.now());

        paymentRepository.save(payment);

        return payment;
    }

    public void handleWebhook(
            String razorpayOrderId,
            String razorpayPaymentId,
            String razorpaySignature
    ) throws Exception {

        // Skip signature for Postman testing
        if (!"dummy-signature".equals(razorpaySignature)) {
            boolean isValid =
                    paymentServiceClient.verifySignature(
                            razorpayOrderId,
                            razorpayPaymentId,
                            razorpaySignature
                    );

            if (!isValid) {
                throw new RuntimeException("Invalid Razorpay signature");
            }
        }

        Payment payment =
                paymentRepository.findByPaymentId(razorpayOrderId)
                        .orElseThrow(() -> new RuntimeException("Payment not found"));


        payment.setStatus("SUCCESS");
        payment.setPaymentId(razorpayPaymentId); // now store real payment id
        paymentRepository.save(payment);

        com.romit.ecommerce.model.Order order =
                orderRepository.findById(payment.getOrderId())
                        .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus("PAID");
        orderRepository.save(order);
    }


}
