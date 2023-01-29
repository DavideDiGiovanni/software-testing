package com.davidedigiovanni.testing.payment;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public interface CardPaymentCharger {

    CardPaymentCharge cardCharge(
            String source,
            BigDecimal amount,
            Currency currency,
            String description
    );
}
