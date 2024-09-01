package tobyspring.hellospring.order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    void save(Order order);

    Optional<Order> findById(Long id);

    List<Order> findAll();
}
