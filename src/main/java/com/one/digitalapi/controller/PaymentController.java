package com.one.digitalapi.controller;

import com.one.digitalapi.service.PaymentService;
import com.one.digitalapi.logger.DefaultLogger;
import com.razorpay.RazorpayClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/payment")
@Tag(name = "Payment Management", description = "APIs for managing payment")
public class PaymentController {

    private static final String CLASSNAME = "PaymentController";
    private static final DefaultLogger LOGGER = new DefaultLogger(PaymentController.class);

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private RazorpayClient razorpayClient;

    @GetMapping("/createOrder")
    @Operation(summary = "Create Order for payment", description = "Create Order for payment")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> createOrder(@RequestParam double amount, @RequestParam String currency) {
        final String methodName = "createOrder";
        LOGGER.infoLog(CLASSNAME, methodName, "Received request to create order with amount: "
                + amount + " and currency: " + currency);
        try {
            // PaymentService.createOrder returns order details as a JSON string.
            String orderResponse = paymentService.createOrder(amount, currency);
            JSONObject orderJson = new JSONObject(orderResponse);
            LOGGER.infoLog(CLASSNAME, methodName, "Order created successfully: " + orderJson.getString("id"));
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("order", orderJson.toMap());
            return ResponseEntity.ok(responseMap);
        } catch (IllegalArgumentException ex) {
            LOGGER.warnLog(CLASSNAME, methodName, "Invalid input provided: " + ex.getMessage());
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("error", "Invalid input");
            errorMap.put("message", ex.getMessage());
            return ResponseEntity.badRequest().body(errorMap);
        } catch (Exception ex) {
            LOGGER.errorLog(CLASSNAME, methodName, "Error occurred while creating order: " + ex.getMessage());
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("error", "Failed to create order");
            errorMap.put("message", ex.getMessage());
            return ResponseEntity.internalServerError().body(errorMap);
        }
    }

    @PostMapping("/verify")
    @Operation(summary = "Verify a Payment", description = "Verify Payment using orderID, paymentId and razorpaySignature")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> verifyPayment(@RequestParam String orderId,
                                           @RequestParam String paymentId,
                                           @RequestParam String razorpaySignature) {
        final String methodName = "verifyPayment";
        LOGGER.infoLog(CLASSNAME, methodName, "Received request to verify payment for orderId: " + orderId);
        try {
            boolean isValid = paymentService.verifyPayment(orderId, paymentId, razorpaySignature);
            Map<String, Object> responseMap = new HashMap<>();
            if (isValid) {
                LOGGER.infoLog(CLASSNAME, methodName, "Payment verified successfully for orderId: " + orderId);
                responseMap.put("message", "Payment verified successfully");
                responseMap.put("status", "success");
                return ResponseEntity.ok(responseMap);
            } else {
                LOGGER.warnLog(CLASSNAME, methodName, "Payment verification failed for orderId: " + orderId);
                responseMap.put("message", "Payment verification failed");
                responseMap.put("status", "failure");
                return ResponseEntity.badRequest().body(responseMap);
            }
        } catch (IllegalArgumentException ex) {
            LOGGER.warnLog(CLASSNAME, methodName, "Invalid input provided: " + ex.getMessage());
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("error", "Invalid input");
            errorMap.put("message", ex.getMessage());
            return ResponseEntity.badRequest().body(errorMap);
        } catch (Exception ex) {
            LOGGER.errorLog(CLASSNAME, methodName, "Error verifying payment: " + ex.getMessage());
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("error", "Error verifying payment");
            errorMap.put("message", ex.getMessage());
            return ResponseEntity.internalServerError().body(errorMap);
        }
    }

    @GetMapping("/fetchOrder")
    @Operation(summary = "Fetch Order", description = "Fetch Order Using Order Id")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> fetchOrder(@RequestParam String orderId) {
        final String methodName = "fetchOrder";
        LOGGER.infoLog(CLASSNAME, methodName, "Received request to fetch order with orderId: " + orderId);
        try {
            JSONObject orderDetails = razorpayClient.orders.fetch(orderId).toJson();
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("order", orderDetails.toMap());
            return ResponseEntity.ok(responseMap);
        } catch (Exception ex) {
            LOGGER.errorLog(CLASSNAME, methodName, "Error fetching order: " + ex.getMessage());
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("error", "Error fetching order details");
            errorMap.put("message", ex.getMessage());
            return ResponseEntity.internalServerError().body(errorMap);
        }
    }



    @PostMapping("/refund")
    @Operation(summary = "Refund a Payment", description = "Initiates refund for a given payment ID")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> refundPayment(@RequestParam String paymentId, @RequestParam double amount) {
        final String methodName = "refundPayment";
        LOGGER.infoLog(CLASSNAME, methodName, "Received request to refund payment: " + paymentId);
        try {
            JSONObject refund = paymentService.refundPayment(paymentId, amount);
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("refund", refund.toMap());
            return ResponseEntity.ok(responseMap);
        } catch (Exception ex) {
            LOGGER.errorLog(CLASSNAME, methodName, "Error processing refund: " + ex.getMessage());
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("error", "Refund failed");
            errorMap.put("message", ex.getMessage());
            return ResponseEntity.internalServerError().body(errorMap);
        }
    }

    // Global Exception Handler (optional) for unhandled exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handlePaymentException(Exception ex) {
        final String methodName = "handlePaymentException";
        LOGGER.errorLog(CLASSNAME, methodName, "Unhandled exception: " + ex.getMessage());
        Map<String, Object> errorMap = new HashMap<>();
        errorMap.put("error", "An unexpected error occurred in the Payment module");
        errorMap.put("message", ex.getMessage());
        return ResponseEntity.internalServerError().body(errorMap);
    }
}