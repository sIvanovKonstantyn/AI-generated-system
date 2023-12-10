package com.demos.paymentsprocessingservice.payments.models;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class PaymentDetails {

    private String paymentId;
    private String serviceId;
    private int amount;
    private String status;
    private String serviceName;
    private LocalDateTime dateTime;

    public PaymentDetails(PaymentHistory paymentHistory) {
       this.paymentId = paymentHistory.getId();
       this.status = paymentHistory.getPaymentState().name();
       this.dateTime = paymentHistory.getCreatedAt();
    }
}