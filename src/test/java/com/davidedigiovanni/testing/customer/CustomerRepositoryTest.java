package com.davidedigiovanni.testing.customer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest(
        properties = {
                "spring.jpa.properties.javax.persistence.validation.mode=none"
        }
)
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository underTest;

    @Test
    void itShouldSaveCustomer() {
        // Given
        UUID id = UUID.randomUUID();
        String name = "Dave";
        String phoneNumber = "0123456789";
        Customer customer = new Customer(id, name, phoneNumber);

        // When
        underTest.save(customer);

        // Then
        Optional<Customer> optionalCustomer = underTest.findById(id);
        assertThat(optionalCustomer)
                .isPresent()
                .hasValueSatisfying(c -> {
//                    assertThat(c.getId()).isEqualTo(id);
//                    assertThat(c.getName()).isEqualTo(name);
//                    assertThat(c.getPhoneNumber()).isEqualTo(phoneNumber);
                    assertThat(c).isEqualToComparingFieldByField(customer);
                });
    }

    @Test
    void itShouldNotSaveCustomerWhenNameIsNull() {
        // Given
        UUID id = UUID.randomUUID();
        String phoneNumber = "0123456789";
        Customer customer = new Customer(id, null, phoneNumber);

        // When
        // Then
        assertThatThrownBy(() -> underTest.save(customer))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessageContaining("not-null property references a null or transient value : com.davidedigiovanni.testing.customer.Customer.name");
    }

    @Test
    void itShouldSelectCustomerByPhoneNumber() {
        // Given
        UUID id = UUID.randomUUID();
        String name = "Abel";
        String phoneNumber = "0000";
        Customer customer = new Customer(id, name, phoneNumber);
        underTest.save(customer);

        // When
        Optional<Customer> customerOptional = underTest.selectCustomerByPhoneNumber(phoneNumber);

        // Then
        assertThat(customerOptional)
                .isPresent()
                .hasValueSatisfying(c -> {
                    assertThat(c).isEqualToComparingFieldByField(customer);
                });
    }

    @Test
    void itNotShouldSelectCustomerByPhoneNumberWhenPhoneNumberDoesNotExist() {
        // Given
        String phoneNumber = "0000";

        // When
        Optional<Customer> customerOptional = underTest.selectCustomerByPhoneNumber(phoneNumber);

        // Then
        assertThat(customerOptional)
                .isNotPresent();
    }
}