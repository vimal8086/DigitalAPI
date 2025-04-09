package com.one.digitalapi.service;

import com.one.digitalapi.config.RazorpayConfig;
import com.one.digitalapi.utils.Utils;
import com.one.digitalapi.logger.DefaultLogger;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private static final DefaultLogger LOGGER = new DefaultLogger(PaymentService.class);

    @Autowired
    private RazorpayClient razorpayClient;

    private final RazorpayConfig razorpayConfig;

    public PaymentService(RazorpayConfig razorpayConfig) {
        this.razorpayConfig = razorpayConfig;
    }

    public String createOrder(double amount, String currency) throws Exception {
        final String methodName = "createOrder";
        LOGGER.infoLog("PaymentService", methodName, "Creating order with amount: " + amount + " and currency: " + currency);

        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amount * 100);  // converting to smallest currency unit
        orderRequest.put("currency", currency);
        orderRequest.put("payment_capture", 1);

        Order order = razorpayClient.orders.create(orderRequest);
        LOGGER.infoLog("PaymentService", methodName, "Order created: " + order.toString());
        return order.toString();
    }

    public boolean verifyPayment(String orderId, String paymentId, String razorpaySignature) {
        final String methodName = "verifyPayment";
        LOGGER.infoLog("PaymentService", methodName, "Verifying payment for orderId: " + orderId);

        String payload = orderId + '|' + paymentId;
        try {
            boolean valid = Utils.verifySignature(payload, razorpaySignature, razorpayConfig.getKeySecret());
            LOGGER.infoLog("PaymentService", methodName, "Payment verification result: " + valid);
            return valid;
        } catch (Exception e) {
            LOGGER.errorLog("PaymentService", methodName, "Error verifying payment signature: " + e.getMessage());
            return false;
        }
    }
}