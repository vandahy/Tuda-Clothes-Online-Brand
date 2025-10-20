package chubby.teu.tuda.manager.repository;

import chubby.teu.tuda.manager.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, String> {
}
