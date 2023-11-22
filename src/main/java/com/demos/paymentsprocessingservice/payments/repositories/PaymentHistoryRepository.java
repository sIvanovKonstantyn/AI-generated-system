package com.demos.paymentsprocessingservice.payments.repositories;

import com.demos.paymentsprocessingservice.payments.models.PaymentHistory;

public interface PaymentHistoryRepository {
    void save(PaymentHistory paymentHistory);
}
