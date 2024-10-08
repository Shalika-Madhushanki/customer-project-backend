package com.example.test_assignment.controller;

import lombok.extern.slf4j.Slf4j;
import com.example.test_assignment.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.test_assignment.service.CustomerService;

import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        log.info("Creating a new customer with name: {}", customer.getName());
        Customer createdCustomer = customerService.createCustomer(customer);
        log.info("Customer created with ID: {}", createdCustomer.getId());
        return ResponseEntity.ok(createdCustomer);
    }

    @GetMapping
    public List<Customer> getAllCustomers() {
        log.info("Fetching all customers");
        List<Customer> customers = customerService.getAllCustomers();
        log.info("Fetched {} customers", customers.size());
        return customers;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
        log.info("Fetching customer with ID: {}", id);
        Optional<Customer> customer = customerService.getCustomerById(id);
        log.info("Customer with ID: {} found", id);
        return customer.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@RequestBody Customer customer, @PathVariable Long id) {
        log.info("Updating customer with ID: {}", id);
        try {
            Customer updatedCustomer = customerService.updateCustomer(customer, id);
            log.info("Customer with ID: {} updated successfully", id);
            return ResponseEntity.ok(updatedCustomer);
        } catch (Exception e) {
            log.warn("Customer with ID: {} not found for update", id);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable Long id) {
        if (customerService.getCustomerById(id).isPresent()) {
            customerService.deleteCustomerById(id);
            log.info("Customer with ID: {} deleted successfully", id);
            return ResponseEntity.ok("Customer with ID " + id + " deleted successfully");
        } else {
            log.warn("Customer with ID: {} not found for deletion", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer with ID " + id + " not found");
        }
    }
}
