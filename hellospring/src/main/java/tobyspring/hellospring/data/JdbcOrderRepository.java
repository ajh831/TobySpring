package tobyspring.hellospring.data;

import jakarta.annotation.PostConstruct;
import org.springframework.jdbc.core.simple.JdbcClient;
import tobyspring.hellospring.order.Order;
import tobyspring.hellospring.order.OrderRepository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

public class JdbcOrderRepository implements OrderRepository {
    private final JdbcClient jdbcClient;

    public JdbcOrderRepository(DataSource dataSource) {
        this.jdbcClient = JdbcClient.create(dataSource);
    }

    @PostConstruct // Constructor가 다 실행되고 빈의 초기화 작업이 끝난 다음 컨테이너가 자동으로 실행해줌
    void initDb() {
        jdbcClient.sql("""
            create table orders (id bigint not null, no varchar(255), total numeric(38,2), primary key (id));
            alter table if exists orders drop constraint if exists UK_43egxxciqr9ncgmxbdx2avi8n;
            alter table if exists orders add constraint UK_43egxxciqr9ncgmxbdx2avi8n unique (no);
            create sequence orders_SEQ start with 1 increment by 50;
        """).update();
    }

    @Override
    public void save(Order order) {
        Long id = jdbcClient.sql("select next value for orders_SEQ").query(Long.class).single();
        System.out.println("id = " + id);
        order.setId(id);

        jdbcClient.sql("insert into orders (no,total,id) values (?,?,?)")
                .params(order.getNo(), order.getTotal(), order.getId())
                .update();
    }

    @Override
    public Optional<Order> findById(Long id) {
        return jdbcClient.sql("select * from orders where id = ?")
                .param(id)  // 쿼리에서 '?'에 해당하는 매개변수로 'id'를 설정
                .query((rs, rowNum) -> {
                    Order order = new Order(rs.getString("no"), rs.getBigDecimal("total"));
                    order.setId(rs.getLong("id"));
                    return order;
                })
                .optional();    // 결과를 Optional로 반환
    }

    @Override
    public List<Order> findAll() {
        return jdbcClient.sql("select * from orders")
                .query((rs, rowNum) -> {
                    // RowMapper 람다식: 쿼리 결과의 각 행을 'Order' 객체로 매핑
                    Order order = new Order(rs.getString("no"), rs.getBigDecimal("total"));
                    order.setId(rs.getLong("id"));
                    return order;
                })
                .list();    // 결과를 List로 반환
    }
}
