package com.demos.paymentsprocessingservice.payments.services;

import com.demos.paymentsprocessingservice.payments.clients.ServiceProviderClient;
import com.demos.paymentsprocessingservice.payments.clients.UserServiceClient;
import com.demos.paymentsprocessingservice.payments.models.PaymentDetails;
import com.demos.paymentsprocessingservice.payments.models.PaymentHistory;
import com.demos.paymentsprocessingservice.payments.models.PaymentRequest;
import com.demos.paymentsprocessingservice.payments.models.PaymentState;
import com.demos.paymentsprocessingservice.payments.providers.PaymentSystemUrlProvider;
import com.demos.paymentsprocessingservice.payments.repositories.PaymentHistoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

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

    @Mock
    private PaymentSystemUrlProvider paymentSystemUrlProvider;

    @InjectMocks
    private PaymentProcessingService paymentProcessingService;

    @Test
    void createPaymentValidPaymentRequestSuccess() throws Exception {
        // Arrange
        PaymentRequest paymentRequest = PaymentRequest.builder()
                .userId("userId")
                .amount(50)
                .description("Description")
                .serviceId("serviceId")
                .build();

        // Mocking external service responses
        when(userServiceClient.getBalance(anyString())).thenReturn(100);
        when(paymentSystemUrlProvider.getPaymentSystemUrl(anyString())).thenReturn("MockPaymentSystem");
        when(paymentHistoryRepository.save(any())).thenReturn(PaymentHistory.builder().id("id").build());

        // Act
        String paymentId = paymentProcessingService.createPayment(paymentRequest);

        // Assert
        verify(paymentHistoryRepository, times(3)).save(any());
        Assertions.assertNotNull(paymentId);
    }

    @Test
    void createPaymentInvalidPaymentAmountException() {
        // Arrange
        PaymentRequest paymentRequest = PaymentRequest.builder()
                .userId("userId")
                .amount(-50)
                .description("Description")
                .build();

        // Act & Assert
        Assertions.assertThrows(Exception.class, () -> paymentProcessingService.createPayment(paymentRequest));

        // Assert
        verify(paymentHistoryRepository, times(1)).save(any());
        verifyNoMoreInteractions(paymentHistoryRepository);
    }

    @Test
    void getPaymentDetails() {
        // Arrange
        String paymentId = "payment123";
        when(paymentHistoryRepository.getById(anyString())).thenReturn(Optional.of(PaymentHistory.builder()
                .paymentState(PaymentState.COMPLETED)
                .build()));

        // Act
        PaymentDetails paymentDetails = paymentProcessingService.getPaymentDetails(paymentId);

        // Assert
        Assertions.assertNotNull(paymentDetails);
    }
}
