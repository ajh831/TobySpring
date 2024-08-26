package tobyspring.hellospring.payment;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.*;
import java.time.temporal.ChronoUnit;

import static java.math.BigDecimal.valueOf;

class PaymentTest {

    @Test
    void createPrepared() {
        Clock clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        ExRateProviderStub exRateProvider = new ExRateProviderStub(valueOf(1_000));

        Payment payment = Payment.createPrepared(
                1L, "USD", BigDecimal.TEN, exRateProvider, clock
        );

        Assertions.assertThat(payment.getConvertedAmount()).isEqualByComparingTo(valueOf(10_000));
        Assertions.assertThat(payment.getValidUntil()).isEqualTo(LocalDateTime.now(clock).plusMinutes(30));
    }

    @Test
    void isValid() {
        Clock clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        ExRateProviderStub exRateProvider = new ExRateProviderStub(valueOf(1_000));

        Payment payment = Payment.createPrepared(
                1L, "USD", BigDecimal.TEN, exRateProvider, clock
        );

        Assertions.assertThat(payment.isValid(clock)).isTrue();
        Assertions.assertThat(
                payment.isValid(Clock.offset(clock, Duration.of(30, ChronoUnit.MINUTES)))).isFalse();
    }
}