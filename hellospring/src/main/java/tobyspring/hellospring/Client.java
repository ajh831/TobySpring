package tobyspring.hellospring;

import java.io.IOException;
import java.math.BigDecimal;

public class Client {
    public static void main(String[] args) throws IOException {
//        PaymentService paymentService = new PaymentService(new WebApiExRateProvider()); // 관계설정의 책임을 Client한테 넘김
        PaymentService paymentService = new PaymentService(new SimpleExRateProvider());
        Payment payment = paymentService.prepare(100L, "USD", BigDecimal.valueOf(50.7));
        System.out.println(payment);
    }
}
