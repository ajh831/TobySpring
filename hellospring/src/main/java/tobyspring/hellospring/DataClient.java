package tobyspring.hellospring;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import tobyspring.hellospring.order.Order;

import java.math.BigDecimal;

public class DataClient {
    public static void main(String[] args) {
        BeanFactory benFactory = new AnnotationConfigApplicationContext(DataConfig.class);
        EntityManagerFactory emf = benFactory.getBean(EntityManagerFactory.class);

        // em 생성
        EntityManager em = emf.createEntityManager();

        // transaction 만들기
        em.getTransaction().begin();

        // em.persist
        Order order = new Order("100", BigDecimal.TEN);

        System.out.println(order);

        em.persist(order);

        System.out.println(order);

        em.getTransaction().commit();
        em.close();
    }
}
