package com.romit.ecommerce.webhook;

import com.romit.ecommerce.dto.PaymentWebhookRequest;
import com.romit.ecommerce.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/webhooks")
public class PaymentWebhookController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/payment")
    public Map<String, Object> handleWebhook(@RequestBody PaymentWebhookRequest request) throws Exception {

        Map<String, Object> response = new HashMap<>();

        if (!"payment.captured".equals(request.getEvent())) {
            response.put("status", "ignored");
            response.put("message", "Event not handled");
            return response;
        }

        String razorpayPaymentId = request.getPayload().getPayment().getEntity().getId();

        String razorpayOrderId = request.getPayload().getPayment().getEntity().getOrder_id();

        paymentService.handleWebhook(razorpayOrderId, razorpayPaymentId, "dummy-signature");

        response.put("status", "success");
        response.put("message", "Payment processed successfully");
        response.put("razorpayOrderId", razorpayOrderId);

        return response;
    }
}
