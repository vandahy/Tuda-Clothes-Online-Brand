package chubby.teu.tuda.feature.manager.repository;

import chubby.teu.tuda.core.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCRUDRepository extends JpaRepository<User, String> {
}
