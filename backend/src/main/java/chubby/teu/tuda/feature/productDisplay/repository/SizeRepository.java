package chubby.teu.tuda.feature.productDisplay.repository;

import chubby.teu.tuda.core.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SizeRepository extends JpaRepository<Size, Integer> {
    Optional<Size> findByName(String name);
}
