package tobyspring.hellospring.order;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tobyspring.hellospring.OrderConfig;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = OrderConfig.class)
class OrderServiceSpringTest {
    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;

    @Test
    public void createOrder() throws Exception {
        var order = orderService.createOrder("0100", BigDecimal.ONE);

        Assertions.assertThat(order.getId()).isGreaterThan(0);
    }

    @Test
    public void findOrderById() throws Exception {
        var order = orderService.createOrder("0101", BigDecimal.TEN);
        var foundOrder = orderRepository.findById(order.getId());

        System.out.println("foundOrder = " + foundOrder);

        Assertions.assertThat(foundOrder).isPresent();
        Assertions.assertThat(foundOrder.get().getNo()).isEqualTo("0101");
    }

    @Test
    public void findAllOrders() throws Exception {
        orderService.createOrder("0101", BigDecimal.TEN);
        orderService.createOrder("0102", BigDecimal.valueOf(20));
        orderService.createOrder("0103", BigDecimal.valueOf(30));

        List<Order> orders = orderRepository.findAll();

        System.out.println(Arrays.toString(orders.toArray()));

        Assertions.assertThat(orders).hasSize(3); // hasSize : 컬렉션 사이즈 확인
        // extracting : 객체의 리스트에서 특정 속성 값을 추출
        // containsExactly : 컬렉션이나 배열의 요소들이 정확히 주어진 순서대로 있는지를 검증
        Assertions.assertThat(orders).extracting("no").containsExactly("0101", "0102", "0103");
    }

}