package tobyspring.hellospring;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.orm.jpa.JpaTransactionManager;
import tobyspring.hellospring.order.Order;
import tobyspring.hellospring.order.OrderService;

import java.math.BigDecimal;

public class OrderClient {
    public static void main(String[] args) {
        BeanFactory benFactory = new AnnotationConfigApplicationContext(OrderConfig.class);
        OrderService service = benFactory.getBean(OrderService.class);
        JpaTransactionManager transactionManager = benFactory.getBean(JpaTransactionManager.class);

        Order order = service.createOrder("0100", BigDecimal.TEN);
        System.out.println("order = " + order);

    }
}