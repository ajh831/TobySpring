package tobyspring.hellospring.payment;

import org.assertj.core.api.Assertions;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tobyspring.hellospring.exrate.WebApiExRateProvider;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static java.math.BigDecimal.valueOf;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class PaymentServiceTest {
    Clock clock;

    @BeforeEach
    void beforeEach() {
        this.clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
    }

    @Test
    @DisplayName("prepare 메소드가 요구사항 3가지를 잘 충족했는지 검증")
    void prepare() {
        PaymentService paymentService = new PaymentService(new WebApiExRateProvider(), this.clock);

        Payment payment = paymentService.prepare(1L, "USD", BigDecimal.TEN);

        // 환율정보 가져오기
        Assertions.assertThat(payment.getExRate()).isNotNull();
        
        // 원화환산금액 계산
        assertThat(payment.getConvertedAmount()) // "환율 X 외환금액"이 "원화 환산금액"과 일치하는지 확인
                .isEqualTo(payment.getExRate().multiply(payment.getForeginCurrencyAmount()));

        // 원화환산금액의 유효시간 계산
        assertThat(payment.getValidUntil()).isAfter(LocalDateTime.now());
        assertThat(payment.getValidUntil()).isBefore(LocalDateTime.now().plusMonths(30));

    }

    @Test
    @DisplayName("금액과 관련하여 convertedAmount 계산을 정확하게 하는지 검증")
    void convertedAmount() {
        Clock clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());

        testAmount(valueOf(500), valueOf(5_000), clock);
        testAmount(valueOf(1_000), valueOf(10_000), clock);
        testAmount(valueOf(3_000), valueOf(30_000), clock);
    }

    @Test
    @DisplayName("시간 검증")
    void validUntil() {
        PaymentService paymentService = new PaymentService(new ExRateProviderStub(valueOf(1_000)), this.clock);

        Payment payment = paymentService.prepare(1L, "USD", BigDecimal.TEN);

        // validUntil이 prepare()보다 30분 뒤로 설정됐는지?
        LocalDateTime now = LocalDateTime.now(this.clock);
        LocalDateTime expectedValidUntil = now.plusMinutes(30);

        Assertions.assertThat(payment.getValidUntil()).isEqualTo(expectedValidUntil);
    }

    @NotNull
    private static void testAmount(BigDecimal exRate, BigDecimal convertedAmount, Clock clock) {
        PaymentService paymentService = new PaymentService(new ExRateProviderStub(exRate), clock);

        Payment payment = paymentService.prepare(1L, "USD", BigDecimal.TEN);

        assertThat(payment.getExRate()).isEqualByComparingTo(exRate);
        assertThat(payment.getConvertedAmount()).isEqualTo(convertedAmount);
    }
}