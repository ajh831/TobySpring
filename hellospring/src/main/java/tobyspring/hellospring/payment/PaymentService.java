package tobyspring.hellospring.payment;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class PaymentService {
    private final ExRateProvider exRateProvider;

    public PaymentService(ExRateProvider exRateProvider) {
        this.exRateProvider = exRateProvider;
    }

    public Payment prepare(Long orderId, String currency, BigDecimal foreginCurrencyAmount) throws IOException {
        BigDecimal exRate = exRateProvider.getExRate(currency);
        BigDecimal convertedAmount = foreginCurrencyAmount.multiply(exRate);
        LocalDateTime validUntil = LocalDateTime.now().plusMinutes(30);

        return new Payment(orderId, currency, foreginCurrencyAmount, exRate, convertedAmount, validUntil);
    }
}
