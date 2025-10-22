package chubby.teu.tuda.feature.manager.repository;

import chubby.teu.tuda.core.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryCRUDRepository extends JpaRepository<Category, String> {
}
