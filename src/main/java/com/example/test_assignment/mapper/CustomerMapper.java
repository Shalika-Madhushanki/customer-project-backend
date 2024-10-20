package com.example.test_assignment.mapper;

import com.example.test_assignment.dao.CreateCustomerRequest;
import com.example.test_assignment.dao.CustomerResponse;
import com.example.test_assignment.dao.UpdateCustomerRequest;
import com.example.test_assignment.model.Customer;

public class CustomerMapper {
    public static Customer toEntity(CreateCustomerRequest request) {
        Customer customer = new Customer();
        customer.setName(request.getName());
        customer.setContact(request.getContact());
        return customer;
    }

    public static CustomerResponse toResponse(Customer customer) {
        CustomerResponse response = new CustomerResponse();
        response.setId(customer.getId());
        response.setName(customer.getName());
        response.setContact(customer.getContact());
        response.setCreation_date(customer.getCreation_date());
        return response;
    }

    public static Customer updateEntity(Customer customer, UpdateCustomerRequest request) {
        if (request.getName() != null) {
            customer.setName(request.getName());
        }
        if (request.getContact() != null) {
            customer.setContact(request.getContact());
        }
        return customer;
    }

}
