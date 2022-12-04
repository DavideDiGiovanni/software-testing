package com.davidedigiovanni.testing.customer;

import com.davidedigiovanni.testing.customer.Customer;
import com.davidedigiovanni.testing.customer.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
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
}