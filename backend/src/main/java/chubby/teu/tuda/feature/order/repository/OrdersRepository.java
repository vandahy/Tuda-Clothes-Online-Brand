package chubby.teu.tuda.feature.order.repository;

import chubby.teu.tuda.core.Order;
import chubby.teu.tuda.feature.order.dto.OrderListRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrdersRepository extends JpaRepository<Order, String> {
    @Query("SELECT new chubby.teu.tuda.feature.order.dto.OrderListRequest(o.orderCode, o.orderDate, o.status, o.totalAmount) "
            +
            "FROM Order o " +
            "WHERE o.user.username = :userId " +
            "ORDER BY o.orderDate DESC")
    List<OrderListRequest> findOrderListByUserId(String userId);

    Optional<Order> findByOrderCode(String orderCode);
}
