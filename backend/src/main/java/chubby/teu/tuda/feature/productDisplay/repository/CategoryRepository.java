package chubby.teu.tuda.feature.productDisplay.repository;

import chubby.teu.tuda.core.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {

}

