package com.demos.paymentsprocessingservice.payments.services;

import com.demos.paymentsprocessingservice.payments.clients.ServiceProviderClient;
import com.demos.paymentsprocessingservice.payments.clients.UserServiceClient;
import com.demos.paymentsprocessingservice.payments.models.PaymentRequest;
import com.demos.paymentsprocessingservice.payments.models.PaymentState;
import com.demos.paymentsprocessingservice.payments.repositories.PaymentHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentProcessingServiceTest {

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private ServiceProviderClient serviceProviderClient;

    @Mock
    private PaymentHistoryRepository paymentHistoryRepository;

    @InjectMocks
    private PaymentProcessingService paymentProcessingService;

    @Test
    void createPaymentValidPaymentRequestSuccess() throws Exception {
        // Arrange
        PaymentRequest paymentRequest = new PaymentRequest("userId", 50, "Description");

        // Mocking external service responses
        when(userServiceClient.getBalance(anyString())).thenReturn(100);

        // Act
        paymentProcessingService.createPayment(paymentRequest);

        // Assert
        verify(paymentHistoryRepository, times(1)).save(any());
    }

    @Test
    void createPaymentInvalidPaymentAmountException() {
        // Arrange
        PaymentRequest paymentRequest = new PaymentRequest("userId", -50, "Description");

        // Act & Assert
        assertThrows(Exception.class, () -> paymentProcessingService.createPayment(paymentRequest));

        // Assert
        verify(paymentHistoryRepository, never()).save(any());
    }
}