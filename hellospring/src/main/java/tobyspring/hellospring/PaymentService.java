package tobyspring.hellospring;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentService {
    private final ExRateProvider exRateProvider;

    public PaymentService(ExRateProvider exRateProvider) {
//        this.exRateProvider = new WebApiExRateProvider(); // 의존관계 설정하는 책임
        this.exRateProvider = exRateProvider;
    }

    public Payment prepare(Long orderId, String currency, BigDecimal foreginCurrencyAmount) throws IOException {
        BigDecimal exRate = exRateProvider.getExRate(currency);
        BigDecimal convertedAmount = foreginCurrencyAmount.multiply(exRate);
        LocalDateTime validUntil = LocalDateTime.now().plusMinutes(30);

        return new Payment(orderId, currency, foreginCurrencyAmount, exRate, convertedAmount, validUntil);
    }
}
