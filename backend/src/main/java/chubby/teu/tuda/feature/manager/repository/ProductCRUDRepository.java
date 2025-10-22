package chubby.teu.tuda.feature.manager.repository;

import chubby.teu.tuda.core.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCRUDRepository extends JpaRepository<Product, String> {
}
