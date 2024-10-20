package com.example.test_assignment.controller;

import com.example.test_assignment.dao.CreateCustomerRequest;
import com.example.test_assignment.dao.CustomerResponse;
import com.example.test_assignment.dao.UpdateCustomerRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.test_assignment.service.CustomerService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(@Valid @RequestBody CreateCustomerRequest request) {
        log.info("Creating a new customer with name: {}", request.getName());
        CustomerResponse response = customerService.createCustomer(request);
        log.info("Customer created with ID: {}", response.getId());
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponse>> getAllCustomers() {
        log.info("Fetching all customers");
        List<CustomerResponse> responses = customerService.getAllCustomers();

        log.info("Fetched {} customers", responses.size());
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable Long id) {
        log.info("Fetching customer with ID: {}", id);
        CustomerResponse response = customerService.getCustomerById(id);
        log.info("Customer with ID: {} found", id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> updateCustomer(@Valid @RequestBody UpdateCustomerRequest customer, @PathVariable Long id) {
        log.info("Updating customer with ID: {}", id);
        try {
            CustomerResponse response = customerService.updateCustomer(customer, id);
            log.info("Customer with ID: {} updated successfully", id);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.warn("Customer with ID: {} not found for update", id);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomerById(id);
        log.info("Customer with ID: {} deleted successfully", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
