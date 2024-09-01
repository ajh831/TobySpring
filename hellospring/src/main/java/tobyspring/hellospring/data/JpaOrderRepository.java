package tobyspring.hellospring.data;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tobyspring.hellospring.order.Order;
import tobyspring.hellospring.order.OrderRepository;

import java.util.List;
import java.util.Optional;

public class JpaOrderRepository implements OrderRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public void save(Order order) {
        em.persist(order);
    }

    @Override
    public Optional<Order> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Order> findAll() {
        return List.of();
    }

}
