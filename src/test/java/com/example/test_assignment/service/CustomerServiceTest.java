package com.example.test_assignment.service;

import com.example.test_assignment.dao.CreateCustomerRequest;
import com.example.test_assignment.dao.CustomerResponse;
import com.example.test_assignment.dao.UpdateCustomerRequest;
import com.example.test_assignment.model.Customer;
import com.example.test_assignment.repository.CustomerRepository;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class CustomerServiceTest {
    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllCustomers() {
        List<Customer> customers = Collections.singletonList(new Customer("John Doe", "john.doe@gmail.com"));
        when(customerRepository.findAll()).thenReturn(customers);

        List<CustomerResponse> result = customerService.getAllCustomers();
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
    }

    @Test
    public void testGetCustomerById() {
        Customer customer = new Customer("John Doe", "john.doe@gmail.com");
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        Optional<Customer> result = customerService.getCustomerEntityById(1L);
        assertTrue(result.isPresent());
        assertEquals("John Doe", result.get().getName());
    }

    @Test
    public void testCreateCustomer() {
        CreateCustomerRequest customerRequest = new CreateCustomerRequest();
        Customer customer = new Customer("John Doe", "john.doe@gmail.com");

        customerRequest.setName("John Doe");
        customerRequest.setContact("john.doe@gmail.com");
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        CustomerResponse createdCustomer = customerService.createCustomer(customerRequest);
        assertEquals("John Doe", createdCustomer.getName());
        assertEquals("john.doe@gmail.com", createdCustomer.getContact());
    }

    @Test
    public void testUpdateCustomer() {
        Long custId = 1L;
        Customer existingCustomer = new Customer();
        existingCustomer.setId(custId);
        existingCustomer.setName("John Doe");
        existingCustomer.setContact("john.doe@gmail.com");

        UpdateCustomerRequest updateRequest = new UpdateCustomerRequest();
        updateRequest.setName("Mick Doe");
        updateRequest.setContact("mick.doe@gmail.com");

        when(customerRepository.findById(custId)).thenReturn(Optional.of(existingCustomer));
        // Update customer details (this would normally be done in the service)
        Customer updatedCustomerEntity = new Customer();
        updatedCustomerEntity.setId(custId);
        updatedCustomerEntity.setName(updateRequest.getName());
        updatedCustomerEntity.setContact(updateRequest.getContact());

        // Mock save behavior for the updated customer
        when(customerRepository.save(any(Customer.class))).thenReturn(updatedCustomerEntity);

        CustomerResponse updatedCustomer = customerService.updateCustomer(updateRequest, custId);
        assertEquals("Mick Doe", updatedCustomer.getName());
        assertEquals("mick.doe@gmail.com", updatedCustomer.getContact());
    }

    @Test
    public void testDeleteCustomer() {
        Long custId = 1L;

        when(customerRepository.existsById(custId)).thenReturn(true);
        doNothing().when(customerRepository).deleteById(custId);

        customerService.deleteCustomerById(custId);

        verify(customerRepository, times(1)).existsById(custId);
        verify(customerRepository, times(1)).deleteById(custId);
    }
}
