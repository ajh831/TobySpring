package tobyspring.hellospring.order;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tobyspring.hellospring.OrderConfig;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = OrderConfig.class)
class OrderServiceSpringTest {
    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    private DataSource dataSource;

    @Test
    public void createOrder() throws Exception {
        var order = orderService.createOrder("O100", BigDecimal.ONE);

        assertThat(order.getId()).isGreaterThan(0);
    }

    @Test
    public void findOrderById() throws Exception {
        var order = orderService.createOrder("O101", BigDecimal.TEN);
        var foundOrder = orderRepository.findById(order.getId());

        System.out.println("foundOrder = " + foundOrder);

        assertThat(foundOrder).isPresent();
        assertThat(foundOrder.get().getNo()).isEqualTo("O101");
    }

    @Test
    public void findAllOrders() throws Exception {
        orderService.createOrder("O101", BigDecimal.TEN);
        orderService.createOrder("O102", BigDecimal.valueOf(20));
        orderService.createOrder("O103", BigDecimal.valueOf(30));

        List<Order> orders = orderRepository.findAll();

        System.out.println(Arrays.toString(orders.toArray()));

        assertThat(orders).hasSize(3); // hasSize : 컬렉션 사이즈 확인
        // extracting : 객체의 리스트에서 특정 속성 값을 추출
        // containsExactly : 컬렉션이나 배열의 요소들이 정확히 주어진 순서대로 있는지를 검증
        assertThat(orders).extracting("no").containsExactly("O101", "O102", "O103");
    }
    
    @Test
    public void createOrders() throws Exception {
        List<OrderReq> orderReqs = List.of(
                new OrderReq("O200", BigDecimal.ONE),
                new OrderReq("O201", BigDecimal.TWO)
        );

        var orders = orderService.createOrders(orderReqs);

        assertThat(orders).hasSize(2);
        orders.forEach(order -> assertThat(order.getId()).isGreaterThan(0));
    }

    @Test
    public void createDuplicatedOrders() throws Exception {
        List<OrderReq> orderReqs = List.of(
                new OrderReq("O300", BigDecimal.ONE),
                new OrderReq("O300", BigDecimal.TWO)
        );

        assertThatThrownBy(() -> orderService.createOrders(orderReqs))
            .isInstanceOf(DataIntegrityViolationException.class);

        JdbcClient client = JdbcClient.create(dataSource);
        var count = client.sql("select count(*) from orders where no = 'O300'").query(Long.class).single();
        assertThat(count).isEqualTo(0);
    }
}