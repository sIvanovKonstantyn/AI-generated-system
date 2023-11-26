package com.demos.paymentsprocessingservice.payments.controllers;

import com.demos.paymentsprocessingservice.common.config.SecurityConfig;
import com.demos.paymentsprocessingservice.payments.models.PaymentDetails;
import com.demos.paymentsprocessingservice.payments.models.PaymentRequest;
import com.demos.paymentsprocessingservice.payments.services.PaymentProcessingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;


@WebMvcTest(PaymentController.class)
@ContextConfiguration(classes = {SecurityConfig.class, PaymentController.class})
@WithMockUser
public class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentProcessingService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreatePayment() throws Exception {
        Mockito.when(service.createPayment(Mockito.any()))
                .thenReturn("id");

        PaymentRequest paymentRequest = PaymentRequest.builder()
                .serviceId("service123")
                .amount(100)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentRequest)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string("id"));
    }

    @Test
    public void testGetAllPayments() throws Exception {
        Mockito.when(service.getAllPayments())
                .thenReturn(List.of(PaymentDetails.builder().build()));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/payments"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json("[{}]")); // Adjust based on your expected response content
    }

    @Test
    public void testGetPaymentDetails() throws Exception {

        Mockito.when(service.getPaymentDetails(Mockito.anyString()))
                        .thenReturn(PaymentDetails.builder().build());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/payments/payment123"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json("{}")); // Adjust based on your expected response content
    }
}