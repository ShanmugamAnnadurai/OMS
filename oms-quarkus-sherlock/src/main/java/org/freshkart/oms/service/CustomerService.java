package org.freshkart.oms.service;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.freshkart.oms.entity.Customer;
import org.freshkart.oms.repository.CustomerRepository;

import java.util.List;
import java.util.Optional;


@ApplicationScoped
public class CustomerService {

    @Inject
    CustomerRepository customerRepository;

    public List<Customer> getAllCustomer() {
        return customerRepository.listAll();
    }

    @Transactional
    public Customer getCustomerById(Long customerId) {
        return customerRepository.findById(customerId);
    }

    @Transactional
    public Customer createCustomer(Customer customer) {
        customerRepository.persist(customer);
        return customer;
    }

    @Transactional
    public Customer updateCustomer(Customer updatedCustomer) {
        Optional<Customer> existingCustomer = Optional.ofNullable(getCustomerById(updatedCustomer.getCustomerId()));
        if (existingCustomer.isPresent()) {
            return customerRepository.getEntityManager().merge(updatedCustomer);
        } else {
            throw new IllegalArgumentException("Customer with ID " + updatedCustomer.getCustomerId() + " does not exist");
        }
    }

    @Transactional
    public boolean deleteCustomer(Long customerId) {
        return customerRepository.deleteById(customerId);
    }


}
