package chubby.teu.tuda.feature.manager.repository;

import chubby.teu.tuda.core.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderCRUDRepository extends JpaRepository<Order, String> {
}
