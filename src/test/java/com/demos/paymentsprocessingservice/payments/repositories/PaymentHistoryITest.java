package com.demos.paymentsprocessingservice.payments.repositories;

import com.demos.paymentsprocessingservice.payments.models.PaymentHistory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
//TODO add test containers
public class PaymentHistoryITest {

    @Autowired
    private PaymentHistoryRepository paymentHistoryRepository;

    @Test
    public void findAllByUserId() {
        // Given
        PaymentHistory paymentHistory = new PaymentHistory();
        paymentHistory.setUserId("1234567890");
        paymentHistoryRepository.save(paymentHistory);

        // When
        List<PaymentHistory> paymentHistories = paymentHistoryRepository.findAllByUserId("1234567890");

        // Then
        assertThat(paymentHistories).hasSize(1);
        assertThat(paymentHistories.get(0).getUserId()).isEqualTo("1234567890");
    }
}