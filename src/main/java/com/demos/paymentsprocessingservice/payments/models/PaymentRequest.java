package com.demos.paymentsprocessingservice.payments.models;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentRequest {
    private String serviceId;
    private int amount;
    private String description;
    private LocalDateTime dateTime;
    private String userId;
}