package tobyspring.hellospring.data;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import tobyspring.hellospring.order.Order;

public class OrderRepository {
    private final EntityManagerFactory emf;

    public OrderRepository(EntityManagerFactory emf) {
        this.emf = emf;
    }

    // 새로운 주문을 DB에 저장
    public void save(Order order) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        try {
            em.persist(order);
            em.flush();

            transaction.commit();
        }
        catch (RuntimeException e) {
            if(transaction.isActive()) transaction.rollback();
            throw e;
        }
        finally {
            if(em.isOpen()) em.close();
        }
    }
}
