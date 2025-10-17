package chubby.teu.tuda.feature.productDisplay.repository;

import chubby.teu.tuda.core.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ProductVariantRepository extends JpaRepository<ProductVariant,Integer> {
    List<ProductVariant> findByProductCode(String productCode);

    Optional<ProductVariant> findByProductCodeAndSizeId(String productCode, Integer sizeId);

    @Query("SELECT pv FROM ProductVariant pv WHERE pv.productCode = :productCode AND pv.isActive = true")
    List<ProductVariant> findActiveVariantsByProductCode(String productCode);
}
