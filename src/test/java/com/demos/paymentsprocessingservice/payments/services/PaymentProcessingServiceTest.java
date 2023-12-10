package com.demos.paymentsprocessingservice.payments.services;

import com.demos.paymentsprocessingservice.payments.clients.ServiceProviderClient;
import com.demos.paymentsprocessingservice.payments.clients.UserServiceClient;
import com.demos.paymentsprocessingservice.payments.exceptions.InvalidPaymentAmountException;
import com.demos.paymentsprocessingservice.payments.exceptions.PaymentProcessingException;
import com.demos.paymentsprocessingservice.payments.models.PaymentDetails;
import com.demos.paymentsprocessingservice.payments.models.PaymentHistory;
import com.demos.paymentsprocessingservice.payments.models.PaymentRequest;
import com.demos.paymentsprocessingservice.payments.models.PaymentState;
import com.demos.paymentsprocessingservice.payments.providers.PaymentSystemUrlProvider;
import com.demos.paymentsprocessingservice.payments.repositories.PaymentHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentProcessingServiceTest {
    @Mock
    private UserServiceClient userServiceClient;
    @Mock
    private ServiceProviderClient serviceProviderClient;
    @Mock
    private PaymentHistoryRepository paymentHistoryRepository;
    @Mock
    private PaymentSystemUrlProvider paymentSystemUrlProvider;
    private PaymentProcessingService paymentProcessingService;

    @BeforeEach
    void setUp() {
        paymentProcessingService = new PaymentProcessingService(
                userServiceClient,
                serviceProviderClient,
                paymentHistoryRepository,
                paymentSystemUrlProvider
        );
    }

    @Test
    void createPayment_ValidPaymentRequest_SuccessfullyProcessesPayment() throws Exception {
        // Arrange
        PaymentRequest paymentRequest = PaymentRequest.builder()
                .userId("user1")
                .serviceId("service1")
                .amount(100)
                .description("Test payment")
                .build();

        String paymentId = "payment1";
        PaymentHistory paymentHistory = PaymentHistory.builder().id(paymentId).build();
        when(paymentHistoryRepository.insert(any(PaymentHistory.class))).thenReturn(paymentHistory);
        when(paymentHistoryRepository.save(any(PaymentHistory.class))).thenReturn(paymentHistory);
        when(userServiceClient.getBalance("user1")).thenReturn(200);
        when(paymentSystemUrlProvider.getPaymentSystemUrl("service1")).thenReturn("http://service1.com");

        // Act
        String result = paymentProcessingService.createPayment(paymentRequest);

        // Assert
        assertEquals(paymentId, result);
        verify(paymentHistoryRepository, times(3)).save(any(PaymentHistory.class));
        verify(serviceProviderClient).sendPayment("http://service1.com", paymentRequest);
        verify(userServiceClient).getBalance("user1");
    }

    @Test
    void createPayment_InvalidPaymentAmount_ThrowsInvalidPaymentAmountException() {
        // Arrange
        PaymentRequest paymentRequest = PaymentRequest.builder()
                .userId("user1")
                .serviceId("service1")
                .amount(-100)
                .description("Test payment")
                .build();
        // Act and Assert
        assertThrows(InvalidPaymentAmountException.class,
                () -> paymentProcessingService.createPayment(paymentRequest));
        verify(paymentHistoryRepository, never()).insert(any(PaymentHistory.class));
        verify(paymentHistoryRepository, never()).save(any(PaymentHistory.class));
        verify(userServiceClient, never()).getBalance(anyString());
        verify(paymentSystemUrlProvider, never()).getPaymentSystemUrl(anyString());
        verify(serviceProviderClient, never()).sendPayment(anyString(), any(PaymentRequest.class));
    }

    @Test
    void createPayment_InsufficientBalance_ThrowsInsufficientBalanceException() throws Exception {
        // Arrange
        PaymentRequest paymentRequest = PaymentRequest.builder()
                .userId("user1")
                .serviceId("service1")
                .amount(200)
                .description("Test payment")
                .build();
        when(userServiceClient.getBalance("user1")).thenReturn(100);
        when(paymentHistoryRepository.insert(any(PaymentHistory.class)))
                .thenReturn(PaymentHistory.builder().id("payment1").build());

        // Act and Assert
        assertThrows(PaymentProcessingException.class,
                () -> paymentProcessingService.createPayment(paymentRequest));
        verify(paymentHistoryRepository, times(1)).insert(any(PaymentHistory.class));
        verify(paymentHistoryRepository, times(1)).save(any(PaymentHistory.class));
        verify(paymentSystemUrlProvider, never()).getPaymentSystemUrl(anyString());
        verify(serviceProviderClient, never()).sendPayment(anyString(), any(PaymentRequest.class));
    }

    @Test
    void createPayment_ExceptionDuringProcessing_ThrowsPaymentProcessingException() throws Exception {
        // Arrange
        PaymentRequest paymentRequest = PaymentRequest.builder()
                .userId("user1")
                .serviceId("service1")
                .amount(100)
                .description("Test payment")
                .build();
        String paymentId = "payment1";
        PaymentHistory paymentHistory = PaymentHistory.builder().id(paymentId).build();
        when(paymentHistoryRepository.insert(any(PaymentHistory.class))).thenReturn(paymentHistory);
        when(paymentHistoryRepository.save(any(PaymentHistory.class))).thenReturn(paymentHistory);
        when(userServiceClient.getBalance("user1")).thenReturn(200);
        when(paymentSystemUrlProvider.getPaymentSystemUrl("service1")).thenReturn("http://service1.com");
        doThrow(new RuntimeException("Error during processing")).when(serviceProviderClient)
                .sendPayment("http://service1.com", paymentRequest);

        // Act and Assert
        assertThrows(PaymentProcessingException.class,
                () -> paymentProcessingService.createPayment(paymentRequest));
        verify(paymentHistoryRepository, times(3)).save(any(PaymentHistory.class));
        verify(serviceProviderClient).sendPayment("http://service1.com", paymentRequest);
        verify(userServiceClient).getBalance("user1");
    }

    @Test
    void getAllPayments_ReturnsAllPaymentDetailsForUser() {
        // Arrange
        String userId = "anonymousUser";
        List<PaymentHistory> paymentHistoryList = new ArrayList<>();
        paymentHistoryList.add(PaymentHistory.builder()
                .id("payment1")
                .userId(userId)
                .paymentDescription("Test payment 1")
                .paymentState(PaymentState.NEW)
                .createdAt(LocalDateTime.parse("2021-01-01T00:00:00"))
                .build());
        paymentHistoryList.add(PaymentHistory.builder()
                .id("payment2")
                .userId(userId)
                .paymentDescription("Test payment 1")
                .paymentState(PaymentState.NEW)
                .createdAt(LocalDateTime.parse("2021-01-01T00:00:00"))
                .build());
        when(paymentHistoryRepository.findAllByUserId(userId)).thenReturn(paymentHistoryList);

        // Act
        List<PaymentDetails> result = paymentProcessingService.getAllPayments();

        // Assert
        assertEquals(2, result.size());
        assertEquals("payment1", result.get(0).getPaymentId());
        assertEquals(LocalDateTime.parse("2021-01-01T00:00:00"), result.get(0).getDateTime());

        assertEquals("payment2", result.get(1).getPaymentId());
        assertEquals(LocalDateTime.parse("2021-01-01T00:00:00"), result.get(1).getDateTime());

        verify(paymentHistoryRepository).findAllByUserId(userId);
    }

    @Test
    void getPaymentDetails_NonExistingPaymentId_ThrowsRuntimeException() {
        // Arrange
        String paymentId = "payment1";
        when(paymentHistoryRepository.findById(paymentId)).thenReturn(java.util.Optional.empty());

        // Act and Assert
        assertThrows(RuntimeException.class,
                () -> paymentProcessingService.getPaymentDetails(paymentId));
        verify(paymentHistoryRepository).findById(paymentId);
    }
}
