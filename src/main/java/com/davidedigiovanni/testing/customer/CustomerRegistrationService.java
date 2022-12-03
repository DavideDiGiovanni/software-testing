package com.davidedigiovanni.testing.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerRegistrationService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerRegistrationService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public void registerNewCustomer(CustomerRegistrationRequest request) {
        Customer requestCustomer = request.getCustomer();
        String requestPhoneNumber = requestCustomer.getPhoneNumber();
        Optional<Customer> optionalCustomer = customerRepository.selectCustomerByPhoneNumber(requestPhoneNumber);

        if (optionalCustomer.isEmpty()) {
            customerRepository.save(requestCustomer);
            return;
        }

        Customer customer = optionalCustomer.get();

        if(requestCustomer.getName().equals(customer.getName())) {
            return;
        }

        throw new IllegalStateException(String.format("Phone number [%s] already taken", requestPhoneNumber));
    }
}
