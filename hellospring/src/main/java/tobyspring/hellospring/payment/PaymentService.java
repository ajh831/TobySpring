package tobyspring.hellospring.payment;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;

@Component
public class PaymentService {
    private final ExRateProvider exRateProvider;
    private final Clock clock;

    public PaymentService(ExRateProvider exRateProvider, Clock clock) {
        this.exRateProvider = exRateProvider;
        this.clock = clock;
    }

    public Payment prepare(Long orderId, String currency, BigDecimal foreginCurrencyAmount) throws IOException {
        BigDecimal exRate = exRateProvider.getExRate(currency);
//        BigDecimal convertedAmount = foreginCurrencyAmount.multiply(exRate);
//        LocalDateTime validUntil = LocalDateTime.now(clock).plusMinutes(30);

        return Payment.createPrepared(orderId, currency, foreginCurrencyAmount, exRate, LocalDateTime.now(clock));
    }
}
