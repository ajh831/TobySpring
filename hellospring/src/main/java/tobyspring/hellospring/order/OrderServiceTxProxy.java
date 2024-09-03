package tobyspring.hellospring.order;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.util.List;

public class OrderServiceTxProxy implements OrderService {
    private final OrderService target;
    private final PlatformTransactionManager transactionManager;

    public OrderServiceTxProxy(OrderService target, PlatformTransactionManager transactionManager) {
        this.target = target;
        this.transactionManager = transactionManager;
    }

    @Override
    public Order createOrder(String no, BigDecimal total) {
        return new TransactionTemplate(transactionManager).execute(status ->
            target.createOrder(no, total)
        );
    }

    @Override
    public List<Order> createOrders(List<OrderReq> reqs) {
        // transaction 시작
        return new TransactionTemplate(transactionManager).execute(status ->
                /*
                    실제 CreateOrders에 해당하는 애플리케이션 서비스의 로직은
                     target에 해당하는 오브젝트 안에 있는 createOrders 메서드를 호출해서 담당하도록 함
                */
                target.createOrders(reqs)
        );
    }
}
