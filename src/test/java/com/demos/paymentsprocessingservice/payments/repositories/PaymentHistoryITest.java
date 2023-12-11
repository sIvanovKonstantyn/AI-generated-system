package com.demos.paymentsprocessingservice.payments.repositories;

import com.demos.paymentsprocessingservice.payments.models.PaymentHistory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class PaymentHistoryITest {

    @Container
    static PostgreSQLContainer<?> dbContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres"))
            .withUsername("postgres")
            .withPassword("postgres");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", dbContainer::getJdbcUrl);
    }

    @Autowired
    private PaymentHistoryRepository paymentHistoryRepository;

    @BeforeEach
    void beforeEach() {
        paymentHistoryRepository.deleteAll();
    }

    @AfterAll
    static void shutDown() {
        dbContainer.close();
    }

    @Test
    public void findAllByUserIdShouldWorkAfterWeInsertedItemIntoDB() {
        // Given
        PaymentHistory paymentHistory = PaymentHistory.newPaymentHistory("1234567890", "description", LocalDateTime.now());
        paymentHistoryRepository.insert(paymentHistory);
        // When
        List<PaymentHistory> paymentHistories = paymentHistoryRepository.findAllByUserId("1234567890");
        // Then
        assertThat(paymentHistories).hasSize(1);
        assertThat(paymentHistories.get(0).getUserId()).isEqualTo("1234567890");
    }
}