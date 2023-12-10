package com.demos.paymentsprocessingservice.payments.repositories;

import com.demos.paymentsprocessingservice.payments.models.PaymentHistory;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PaymentHistoryRepository extends CrudRepository<PaymentHistory, String>, AggregateRepository<PaymentHistory> {

    List<PaymentHistory> findAllByUserId(String userId);
}