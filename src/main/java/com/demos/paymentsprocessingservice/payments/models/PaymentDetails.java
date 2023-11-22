package com.demos.paymentsprocessingservice.payments.models;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class PaymentDetails {

    private String paymentId;
    private String serviceId;
    private int amount;
    private String status;
    private String serviceName;
    private LocalDateTime dateTime;
}