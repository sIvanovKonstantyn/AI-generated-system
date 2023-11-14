package com.demos.paymentsprocessing.services.controllers;

import com.demos.paymentsprocessing.services.models.ServiceProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/service-providers")
public class ServiceProviderController {

    private final List<ServiceProvider> serviceProviders = List.of(new ServiceProvider());

    @PostMapping
    public ResponseEntity<Void> createServiceProvider(@RequestBody ServiceProvider serviceProvider) {
        // Mock logic for creating a service provider
        // Add your actual logic here, e.g., call a service to create the new service provider

        // Assuming successful service provider creation
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<ServiceProvider>> getAllServiceProviders() {
        // Mock logic for getting all service providers
        // Return a list of ServiceProvider or another suitable response based on your actual logic

        return ResponseEntity.ok(serviceProviders);
    }

    @GetMapping("/{serviceProviderId}")
    public ResponseEntity<ServiceProvider> getServiceProviderDetails(@PathVariable String serviceProviderId) {
        // Mock logic for getting service provider details
        // Return a ServiceProvider or another suitable response based on your actual logic

        return ResponseEntity.ok(new ServiceProvider());
    }

    @PutMapping("/{serviceProviderId}")
    public ResponseEntity<Void> updateServiceProvider(@PathVariable String serviceProviderId, @RequestBody ServiceProvider updatedServiceProvider) {
        // Mock logic for updating a service provider
        // Add your actual logic here, e.g., call a service to update the existing service provider

        // Assuming successful service provider update
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{serviceProviderId}")
    public ResponseEntity<Void> deleteServiceProvider(@PathVariable String serviceProviderId) {
        // Mock logic for deleting a service provider
        // Add your actual logic here, e.g., call a service to delete the existing service provider

        // Assuming successful service provider deletion
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}