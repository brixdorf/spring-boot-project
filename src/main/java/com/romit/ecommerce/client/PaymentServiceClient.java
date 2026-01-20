package com.romit.ecommerce.client;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PaymentServiceClient {

    @Autowired
    private RazorpayClient razorpayClient;

    @Value("${razorpay.key.secret}")
    private String keySecret;

    public Order createRazorpayOrder(String receiptId, double amount) throws Exception {

        JSONObject request = new JSONObject();
        request.put("amount", (int) (amount * 100));
        request.put("currency", "INR");
        request.put("receipt", receiptId);

        return razorpayClient.orders.create(request);
    }

    public boolean verifySignature(String razorpayOrderId, String razorpayPaymentId, String razorpaySignature) throws Exception {

        String payload = razorpayOrderId + "|" + razorpayPaymentId;
        return Utils.verifySignature(payload, razorpaySignature, keySecret);
    }
}
