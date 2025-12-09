package chubby.teu.tuda.feature.orderManager.repository;

import chubby.teu.tuda.core.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderManagementRepository extends JpaRepository<Order, String> {
    
    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.orderDetails od LEFT JOIN FETCH od.product LEFT JOIN FETCH o.payment ORDER BY o.orderDate DESC")
    List<Order> findAllWithDetails();
    
    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.orderDetails od LEFT JOIN FETCH od.product LEFT JOIN FETCH o.payment WHERE o.orderCode = :orderCode")
    Optional<Order> findByIdWithDetails(String orderCode);
}
