package com.demos.paymentsprocessingservice.payments.models;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class PaymentRequest {
    private String serviceId;
    private int amount;
    private String description;
    private LocalDateTime dateTime;
    private String userId;
}