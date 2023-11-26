package com.demos.paymentsprocessingservice.payments.controllers;

import com.demos.paymentsprocessingservice.payments.models.PaymentDetails;
import com.demos.paymentsprocessingservice.payments.models.PaymentRequest;
import com.demos.paymentsprocessingservice.payments.services.PaymentProcessingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/payments", produces = MediaType.APPLICATION_JSON_VALUE)
public class PaymentController {

    private final PaymentProcessingService paymentProcessingService;

    public PaymentController(PaymentProcessingService paymentProcessingService) {
        this.paymentProcessingService = paymentProcessingService;
    }

    @PostMapping
    public ResponseEntity<String> createPayment(@RequestBody PaymentRequest paymentRequest) {
        try {
            // Delegate payment processing to the service
            String paymentId = paymentProcessingService.createPayment(paymentRequest);

            if (paymentId != null) {
                // Assuming successful payment creation
                return ResponseEntity.status(HttpStatus.CREATED).body(paymentId);
            } else {
                // Handle error response
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } catch (Exception e) {
            // Handle exceptions, return appropriate response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<PaymentDetails>> getAllPayments() {
        // Delegate getting all payments to the service
        List<PaymentDetails> paymentDetails = paymentProcessingService.getAllPayments();

        // Return a list of PaymentDetails or another suitable response based on your actual logic
        return ResponseEntity.ok(paymentDetails);
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentDetails> getPaymentDetails(@PathVariable String paymentId) {
        // Delegate getting payment details to the service
        PaymentDetails paymentDetails = paymentProcessingService.getPaymentDetails(paymentId);

        // Return a PaymentDetails or another suitable response based on your actual logic
        return ResponseEntity.ok(paymentDetails);
    }
}