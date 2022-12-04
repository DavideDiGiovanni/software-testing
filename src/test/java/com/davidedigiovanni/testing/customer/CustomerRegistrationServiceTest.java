package com.davidedigiovanni.testing.customer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.never;

class CustomerRegistrationServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Captor
    private ArgumentCaptor<Customer> customerArgumentCaptor;

    private CustomerRegistrationService underTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        underTest = new CustomerRegistrationService(customerRepository);
    }

    @Test
    void itShouldSaveNewCustomer() {
        // Given
        String phoneNumber = "00001111";
        Customer customer = new Customer(UUID.randomUUID(), "Marya", phoneNumber);

        // The request
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(customer);

        // No customer with this phone number
        given(customerRepository.selectCustomerByPhoneNumber(phoneNumber))
                .willReturn(Optional.empty());

        // When
        underTest.registerNewCustomer(request);

        // Then
        then(customerRepository).should().save(customerArgumentCaptor.capture());
        Customer customerArgumentCaptorValue = customerArgumentCaptor.getValue();
        assertThat(customerArgumentCaptorValue).isEqualToComparingFieldByField(customer);
    }

    @Test
    void itShouldNotSaveCustomerWhenCustomerAlreadyExists() {
        // Given
        String phoneNumber = "00001111";
        Customer customer = new Customer(UUID.randomUUID(), "Marya", phoneNumber);

        // The request
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(customer);

        // Customer with same phone number and name
        given(customerRepository.selectCustomerByPhoneNumber(phoneNumber))
                .willReturn(Optional.of(customer));

        // When
        underTest.registerNewCustomer(request);

        // Then
//        then(customerRepository).should().selectCustomerByPhoneNumber(phoneNumber);
//        then(customerRepository).shouldHaveNoMoreInteractions();
        then(customerRepository).should(never()).save(any());
    }

    @Test
    void itShouldThrowWhenPhoneNumberIsTaken() {
        // Given
        String phoneNumber = "00001111";
        Customer customer = new Customer(UUID.randomUUID(), "Marya", phoneNumber);

        // The request
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(customer);

        // Customer with same phone number
        Customer returnedCustomer = new Customer(UUID.randomUUID(), "Dave", phoneNumber);

        given(customerRepository.selectCustomerByPhoneNumber(phoneNumber))
                .willReturn(Optional.of(returnedCustomer));

        // Then
        assertThatThrownBy(() -> underTest.registerNewCustomer(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(String.format("Phone number [%s] already taken", phoneNumber));

        then(customerRepository).should(never()).save(any());
    }
}