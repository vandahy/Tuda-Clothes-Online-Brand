package chubby.teu.tuda.feature.order.repository;

import chubby.teu.tuda.core.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipmentRepository extends JpaRepository<Shipment, Integer> {
}
