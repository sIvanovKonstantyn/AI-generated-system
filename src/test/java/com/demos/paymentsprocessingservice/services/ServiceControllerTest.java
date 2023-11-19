package com.demos.paymentsprocessingservice.services.controllers;

import com.demos.paymentsprocessingservice.services.models.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

@WebMvcTest(ServiceController.class)
@ContextConfiguration(classes = ServiceController.class)
public class ServiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateService() throws Exception {
        Service service = new Service();
        service.setServiceId("service123");
        service.setServiceName("Test Service");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/services")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(service)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string("")); // Adjust this based on your expected response content
    }

    @Test
    public void testGetAllServices() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/services"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json("[{}]")); // Adjust based on your expected response content
    }

    @Test
    public void testGetServiceDetails() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/services/service123"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json("{}")); // Adjust based on your expected response content
    }
}