package com.demos.paymentsprocessingservice.payments.repositories;

import com.demos.paymentsprocessingservice.payments.models.PaymentHistory;

import java.util.List;
import java.util.Optional;

public interface PaymentHistoryRepository {
    PaymentHistory save(PaymentHistory paymentHistory);

    Optional<PaymentHistory> getById(String paymentId);

    List<PaymentHistory> getAllForUser(String userName);
}
