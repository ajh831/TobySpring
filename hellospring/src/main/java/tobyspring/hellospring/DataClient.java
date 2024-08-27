package tobyspring.hellospring;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import tobyspring.hellospring.data.OrderRepository;
import tobyspring.hellospring.order.Order;

import java.math.BigDecimal;

public class DataClient {
    public static void main(String[] args) {
        BeanFactory benFactory = new AnnotationConfigApplicationContext(DataConfig.class);
//        EntityManagerFactory emf = benFactory.getBean(EntityManagerFactory.class);
        OrderRepository repository = benFactory.getBean(OrderRepository.class);

        Order order = new Order("100", BigDecimal.TEN);
        repository.save(order);

        System.out.println(order);

        Order order2 = new Order("100", BigDecimal.ONE);
        repository.save(order2);
    }
}
