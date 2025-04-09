package com.one.digitalapi.controller;

import com.one.digitalapi.service.PaymentService;
import com.one.digitalapi.logger.DefaultLogger;
import com.razorpay.RazorpayClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> createOrder(@RequestParam double amount, @RequestParam String currency) {
        final String methodName = "createOrder";
        LOGGER.infoLog(CLASSNAME, methodName, "Received request to create order with amount: "
                + amount + " and currency: " + currency);
        try {
            String orderId = paymentService.createOrder(amount, currency);
            LOGGER.infoLog(CLASSNAME, methodName, "Order created successfully: " + orderId);
            return ResponseEntity.ok(orderId);
        } catch (IllegalArgumentException ex) {
            LOGGER.warnLog(CLASSNAME, methodName, "Invalid input provided: " + ex.getMessage());
            return ResponseEntity.badRequest().body("Invalid input: " + ex.getMessage());
        } catch (Exception ex) {
            LOGGER.errorLog(CLASSNAME, methodName, "Error occurred while creating order: " + ex.getMessage());
            return ResponseEntity.internalServerError().body("Failed to create order.");
        }
    }

    @PostMapping("/verify")
    @Operation(summary = "Verify a Payment", description = "Verify Payment using orderID, paymentId and razorpaySignature")
    public ResponseEntity<?> verifyPayment(@RequestParam String orderId,
                                           @RequestParam String paymentId,
                                           @RequestParam String razorpaySignature) {
        final String methodName = "verifyPayment";
        LOGGER.infoLog(CLASSNAME, methodName, "Received request to verify payment for orderId: " + orderId);
        try {
            boolean isValid = paymentService.verifyPayment(orderId, paymentId, razorpaySignature);
            if (isValid) {
                LOGGER.infoLog(CLASSNAME, methodName, "Payment verified successfully for orderId: " + orderId);
                return ResponseEntity.ok("Payment verified successfully");
            } else {
                LOGGER.warnLog(CLASSNAME, methodName, "Payment verification failed for orderId: " + orderId);
                return ResponseEntity.badRequest().body("Payment verification failed");
            }
        } catch (IllegalArgumentException ex) {
            LOGGER.warnLog(CLASSNAME, methodName, "Invalid input provided: " + ex.getMessage());
            return ResponseEntity.badRequest().body("Invalid input: " + ex.getMessage());
        } catch (Exception ex) {
            LOGGER.errorLog(CLASSNAME, methodName, "Error verifying payment: " + ex.getMessage());
            return ResponseEntity.internalServerError().body("Error verifying payment.");
        }
    }


    @GetMapping("/fetchOrder")
    @Operation(summary = "Fetch Order", description = "Fetch Order Using Order Id")
    public ResponseEntity<?> fetchOrder(@RequestParam String orderId) {
        try {
            JSONObject orderDetails = razorpayClient.orders.fetch(orderId).toJson();
            return ResponseEntity.ok(orderDetails.toString());
        } catch (Exception ex) {
            LOGGER.errorLog("PaymentController", "fetchOrder", "Error fetching order: " + ex.getMessage());
            return ResponseEntity.internalServerError().body("Error fetching order details.");
        }
    }


    // Global Exception Handler (optional) for unhandled exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handlePaymentException(Exception ex) {
        final String methodName = "handlePaymentException";
        LOGGER.errorLog(CLASSNAME, methodName, "Unhandled exception: " + ex.getMessage());
        return ResponseEntity.internalServerError().body("An unexpected error occurred in the Payment module.");
    }
}