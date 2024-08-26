package tobyspring.hellospring.payment;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;

public class Payment {
    // 주문번호, 외국 통화 종류, 외국 통화 기준 결제 금액
    private Long orderId; // 주문번호
    private String currency; // 외국 통화 종류
    private BigDecimal foreginCurrencyAmount; // 정확한 계산(연산)을 위해서 BigDecimal을 사용해야 됨
    private BigDecimal exRate; // 적용 환율
    private BigDecimal convertedAmount; // 원화 환산 금액
    private LocalDateTime validUntil; // 원화 환산 금액 유효 시간

    public Payment(Long orderId, String currency, BigDecimal foreginCurrencyAmount, BigDecimal exRate, BigDecimal convertedAmount, LocalDateTime validUntil) {
        this.orderId = orderId;
        this.currency = currency;
        this.foreginCurrencyAmount = foreginCurrencyAmount;
        this.exRate = exRate;
        this.convertedAmount = convertedAmount;
        this.validUntil = validUntil;
    }

    // 핵심 로직
    // 생성자를 이용, FactoryMethod 이용
    // FactoryMehthod를 이용하면 그 안에 의미있는 처리를 하는 코드도 넣을 수 있으며, 이름을 부여할 수 있음
    // 반드시 static
    public static Payment createPrepared(Long orderId, String currency, BigDecimal foreginCurrencyAmount, BigDecimal exRate,
                                         LocalDateTime now) {
        // 환율정보, foreginCurrencyAmount는 결국 paymet 안에 들어가는 정보이므로
        // 외부에서 payment 안에 들어갈 정보를 가지고 계산을해서 결과 값을 넣어주는 것보다
        // payment 자기가 가지고 있는 정보로 뭔가 게산하거나 수행하는 건 payment 안에 기능이 들어가있는게 좋음
        BigDecimal convertedAmount = foreginCurrencyAmount.multiply(exRate);
        LocalDateTime validUntil = now.plusMinutes(30);

        return new Payment(orderId, currency, foreginCurrencyAmount, exRate, convertedAmount, validUntil);
    }

    public boolean isValid(Clock clock) {
        return LocalDateTime.now(clock).isBefore(validUntil);
    }


    public Long getOrderId() {
        return orderId;
    }

    public String getCurrency() {
        return currency;
    }

    public BigDecimal getForeginCurrencyAmount() {
        return foreginCurrencyAmount;
    }

    public BigDecimal getExRate() {
        return exRate;
    }

    public BigDecimal getConvertedAmount() {
        return convertedAmount;
    }

    public LocalDateTime getValidUntil() {
        return validUntil;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "orderId=" + orderId +
                ", currency='" + currency + '\'' +
                ", foreginCurrencyAmount=" + foreginCurrencyAmount +
                ", exRate=" + exRate +
                ", convertedAmount=" + convertedAmount +
                ", validUntil=" + validUntil +
                '}';
    }
}
