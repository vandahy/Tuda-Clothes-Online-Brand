package chubby.teu.tuda.feature.order.repository;

import chubby.teu.tuda.core.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory, Integer> {
}
