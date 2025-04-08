package com.one.digitalapi.service;

import com.one.digitalapi.entity.PaymentRequest;
import com.one.digitalapi.entity.PaymentResponse;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class RazorpayPaymentService implements PaymentService {

    //@Value("${razorpay.api.key}")
    private String razorpayApiKey;

    //@Value("${razorpay.api.secret}")
    private String razorpayApiSecret;

    @Override
    public PaymentResponse processPayment(PaymentRequest request) {
        try {
            RazorpayClient razorpay = new RazorpayClient(razorpayApiKey, razorpayApiSecret);

            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", request.getAmount() * 100); // Convert to paise
            orderRequest.put("currency", request.getCurrency());
            orderRequest.put("payment_capture", 1);

            Order order = razorpay.orders.create(orderRequest);

            return new PaymentResponse(order.get("id"), "Success", "Order Created");
        } catch (RazorpayException e) {
            return new PaymentResponse(null, "Failure", e.getMessage());
        }
    }
}