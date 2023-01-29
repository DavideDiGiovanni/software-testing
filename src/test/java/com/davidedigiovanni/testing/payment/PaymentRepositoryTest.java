package com.davidedigiovanni.testing.payment;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(
        properties = {
                "spring.jpa.properties.javax.persistence.validation.mode=none"
        }
)
class PaymentRepositoryTest {

    @Autowired
    private PaymentRepository underTest;

    @Test
    void itShouldInsertPayment() {
        // Given
        Payment payment = new Payment(
                1L,
                UUID.randomUUID(),
                new BigDecimal("10.00"),
                Currency.USD,
                "Credit card",
                "Donation"
        );

        // When
        underTest.save(payment);

        // Then
        Optional<Payment> paymentOptional = underTest.findById(1L);
        assertThat(paymentOptional)
                .isPresent()
                .hasValueSatisfying(p ->
                    assertThat(p).isEqualToComparingFieldByField(payment)
                );
    }
}