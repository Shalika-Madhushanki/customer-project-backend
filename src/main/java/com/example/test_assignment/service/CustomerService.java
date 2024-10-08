package com.example.test_assignment.service;

import com.example.test_assignment.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.test_assignment.repository.CustomerRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Optional<Customer> getCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public Customer updateCustomer(Customer customer, Long id) {
        Customer existingCustomer = customerRepository.findById(id).orElseThrow();
        existingCustomer.setName(customer.getName());
        existingCustomer.setContact(customer.getContact());
        return customerRepository.save(existingCustomer);
    }

    public void deleteCustomerById(Long id) {
        customerRepository.deleteById(id);
    }
}
