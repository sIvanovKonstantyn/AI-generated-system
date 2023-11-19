package com.demos.paymentsprocessingservice.serviceproviders.controllers;

import com.demos.paymentsprocessingservice.serviceproviders.models.ServiceProvider;
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

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(ServiceProviderController.class)
@ContextConfiguration(classes = ServiceProviderController.class)
public class ServiceProviderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateServiceProvider() throws Exception {
        ServiceProvider serviceProvider = new ServiceProvider();
        serviceProvider.setId("provider123");
        serviceProvider.setName("Test Provider");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/service-providers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(serviceProvider)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string("")); // Adjust this based on your expected response content
    }

    @Test
    public void testGetAllServiceProviders() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/service-providers"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json("[{}]")); // Adjust based on your expected response content
    }

    @Test
    public void testGetServiceProviderDetails() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/service-providers/provider123"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json("{}")); // Adjust based on your expected response content
    }

    @Test
    public void testUpdateServiceProvider() throws Exception {
        ServiceProvider updatedServiceProvider = new ServiceProvider();
        updatedServiceProvider.setId("provider123");
        updatedServiceProvider.setName("Updated Provider");

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/service-providers/provider123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedServiceProvider)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("")); // Adjust this based on your expected response content
    }

    @Test
    public void testDeleteServiceProvider() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/service-providers/provider123"))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.content().string("")); // Adjust this based on your expected response content
    }
}