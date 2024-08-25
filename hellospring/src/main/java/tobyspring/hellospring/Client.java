package tobyspring.hellospring;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;
import java.math.BigDecimal;

public class Client {
    public static void main(String[] args) throws IOException {
        BeanFactory benFactory = new AnnotationConfigApplicationContext(ObjectFactory.class);
        PaymentService paymentService = benFactory.getBean(PaymentService.class);
        PaymentService paymentService2 = benFactory.getBean(PaymentService.class);

        System.out.println("paymentService = " + paymentService);
        System.out.println("paymentService2 = " + paymentService2);
        System.out.println("paymentService == paymentService2 = " + (paymentService == paymentService2));

        Payment payment = paymentService.prepare(100L, "USD", BigDecimal.valueOf(50.7));
        System.out.println(payment);
    }
}
