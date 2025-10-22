package chubby.teu.tuda.feature.order.repository;

import chubby.teu.tuda.core.Payment;
import chubby.teu.tuda.core.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    Optional<Payment> findByOrder(Order order);
    Optional<Payment> findByOrder_OrderCode(String orderCode);

}
