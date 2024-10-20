package com.example.test_assignment.service;

import com.example.test_assignment.dao.CreateCustomerRequest;
import com.example.test_assignment.dao.CustomerResponse;
import com.example.test_assignment.dao.UpdateCustomerRequest;
import com.example.test_assignment.exception.ResourceNotFoundException;
import com.example.test_assignment.exception.UniqueConstraintViolationException;
import com.example.test_assignment.mapper.CustomerMapper;
import com.example.test_assignment.model.Customer;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import com.example.test_assignment.repository.CustomerRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<CustomerResponse> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream()
                .map(CustomerMapper::toResponse)
                .collect(Collectors.toList());
    }

    public CustomerResponse getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Customer with id {} not found"));
        return CustomerMapper.toResponse(customer);
    }

    public CustomerResponse createCustomer(CreateCustomerRequest customerRequest) {
        try {
            Customer customer = CustomerMapper.toEntity(customerRequest);
            customer = customerRepository.save(customer);
            return CustomerMapper.toResponse(customer);
        } catch (DataIntegrityViolationException ex) {
            throw new UniqueConstraintViolationException("A customer with the same name already exists.");
        }
    }

    public CustomerResponse updateCustomer(UpdateCustomerRequest customerRequest, Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow();
        customer = CustomerMapper.updateEntity(customer, customerRequest);
        customer =  customerRepository.save(customer);
        return CustomerMapper.toResponse(customer);
    }

    public void deleteCustomerById(Long id) {
        if (customerRepository.existsById(id)) {
            customerRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Customer with ID " + id + " not found.");
        }
    }
    public Optional<Customer> getCustomerEntityById(Long customerId) {
        return customerRepository.findById(customerId);
    }
}
