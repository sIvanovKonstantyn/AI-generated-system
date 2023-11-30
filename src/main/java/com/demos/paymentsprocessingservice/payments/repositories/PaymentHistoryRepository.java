package com.demos.paymentsprocessingservice.payments.repositories;

import com.demos.paymentsprocessingservice.payments.models.PaymentHistory;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentHistoryRepository extends CrudRepository<PaymentHistory, String> {
    PaymentHistory save(PaymentHistory paymentHistory);

    Optional<PaymentHistory> getById(String paymentId);

    @Query("SELECT * FROM Payment WHERE userId = :userId")
    List<PaymentHistory> getAllForUser(String userName);
}